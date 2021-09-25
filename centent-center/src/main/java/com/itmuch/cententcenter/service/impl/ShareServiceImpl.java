package com.itmuch.cententcenter.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.itmuch.cententcenter.DTO.Centent.ShareAuditDTO;
import com.itmuch.cententcenter.DTO.Centent.ShareDTO;
import com.itmuch.cententcenter.DTO.User.UserAddBonusDTO;
import com.itmuch.cententcenter.DTO.User.UserDTO;
import com.itmuch.cententcenter.DTO.enums.AuditStatusEnum;
import com.itmuch.cententcenter.DTO.messaging.UserAddBonusMsgDTO;
import com.itmuch.cententcenter.entity.MidUserShare;
import com.itmuch.cententcenter.entity.RocketmqTransactionLog;
import com.itmuch.cententcenter.entity.Share;
import com.itmuch.cententcenter.feignClients.UserCenterFeignClient;
import com.itmuch.cententcenter.mapper.MidUserShareMapper;
import com.itmuch.cententcenter.mapper.RocketmqTransactionLogMapper;
import com.itmuch.cententcenter.mapper.ShareMapper;
import com.itmuch.cententcenter.service.IShareService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.apache.rocketmq.spring.support.RocketMQHeaders;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * <p>
 * 分享表 服务实现类
 * </p>
 *
 * @author Polaris-ST
 * @since 2021-08-04
 */
@Service
@RequiredArgsConstructor(onConstructor = @_(@Autowired))
//@Transactional(rollbackFor = Exception.class)
public class ShareServiceImpl extends ServiceImpl<ShareMapper, Share> implements IShareService {
    private final ShareMapper shareMapper;
    private final UserCenterFeignClient userCenterFeignClient;

    //    @Cacheable(cacheNames = {"share"})
    public ShareDTO selectById(Integer id) {   //查看此次分享到信息
        Share share = shareMapper.selectById(id);
        //找到此次信息的用户id
        Integer userId = share.getUserId();
        //Feign来查询实例
        UserDTO userDTO = userCenterFeignClient.findByID(userId);
        //消息装配
        ShareDTO shareDTO = new ShareDTO();
        BeanUtils.copyProperties(share, shareDTO);
        shareDTO.setWxNickName(userDTO.getWxNickname());
        return shareDTO;
    }

    private final RocketMQTemplate rocketMQTemplate;
    private final RocketmqTransactionLogMapper transactionLogMapper;
    private final Source source;
    final private MidUserShareMapper midUserShareMapper;
    //     @Transactional(rollbackFor = Exception.class)
    public Share auditById(Integer id, ShareAuditDTO shareAuditDTO) {

        // 1 查询share是否存在  不存在或者audit-status！=NOT_YET 抛出异常
        Share share = shareMapper.selectById(id);
        if (share == null) {
            throw new IllegalArgumentException("非法参数,分享不存在非法参数");
        }
        if (!Objects.equals(share.getAuditStatus(), "NOT_YET")) {
            throw new IllegalArgumentException("非法参数,分享已经审核");
        }
        //2 审核通过时候执行rocketMQ 第一步 第二步操作
        if (shareAuditDTO.getAuditStatusEnum().equals(AuditStatusEnum.PASS)) {
            String transactionID = UUID.randomUUID().toString();
            source.output().send(
                    MessageBuilder.withPayload(UserAddBonusMsgDTO
                            .builder()
                            .userID(share.getUserId())
                            .bonus(100).build())
                            //将arg的参数用header传送
                            .setHeader(RocketMQHeaders.TRANSACTION_ID, transactionID)
                            .setHeader("share_id", share.getId())
                            //把他先转化成JSON字符串不然事务处理那收不到
                            .setHeader("dto", JSON.toJSONString(shareAuditDTO))
                            .build()
            );

        } else {
            auditByIdInDB(shareAuditDTO, id);
        }
        return share;
    }

    //3 审核资源
    @Transactional(rollbackFor = Exception.class)
    public void auditByIdInDB(ShareAuditDTO shareAuditDTO, Integer id) {
        Share share = Share.builder().id(id)
                .auditStatus(shareAuditDTO.getAuditStatusEnum().toString())
                .reason(shareAuditDTO.getReason())
                .build();
        int i = shareMapper.updateByIdSelective(share);
        if (i > 0) {
            System.out.println("true");
        } else {
            System.out.println("false");
        }
        //4 把share写入缓存
    }

    //用记录日志的方式审核资源
    @Transactional(rollbackFor = Exception.class)
    public void auditByIdWithRocketMqLog(ShareAuditDTO shareAuditDTO, Integer id, String transaction_ID) {
        auditByIdInDB(shareAuditDTO, id);
        transactionLogMapper.insertMy(RocketmqTransactionLog.builder()
                .log("审核分享")
                .transactionID(transaction_ID)
                .build());


    }

    public PageInfo<Share> q(String title, Integer pageNo, Integer pageSize, Integer uid) {
        //他会切入下面那条未分页的sql 利用mybatis的拦截器进行sql语句拼接
        PageHelper.startPage(pageNo,pageSize);
        //未分页的数据
        List<Share> shares = this.shareMapper.selectByParam(title);
        //如果用户没有登录下载链接为null
        List<Share> sharesDealed;
        if (uid==null) {
            sharesDealed = shares.stream().peek(share -> share.setDownloadUrl(null)).collect(Collectors.toList());
        }
        //如果用户登陆了，但是没有兑换的话 下载链接也要为null,也就是mid_user_share也为null
        else {
            sharesDealed =   shares.stream().peek(share -> {
                MidUserShare midUserShare = midUserShareMapper.selectOneMidUserShare(MidUserShare.builder()
                        .shareId(share.getId())
                        .userId(uid)
                        .build());
                if (midUserShare==null)
                    share.setDownloadUrl("null");
            }).collect(Collectors.toList());
        }
        return new  PageInfo<Share>(sharesDealed);

    }

    public Share exchangeById(Integer id, HttpServletRequest request) {
        // TODO: 2021/9/23 1. 根据id查询share,校验是否存在
        Share share = this.shareMapper.selectById(id);
        String userID = (String) request.getAttribute("id");
        Integer uID = Integer.valueOf(userID);
        if (share==null)
            throw new IllegalArgumentException("该分享不存在");
        // TODO: 2021/9/23 如果当前用户兑换过直接返回
        MidUserShare midUserShare = midUserShareMapper.selectOneMidUserShare(MidUserShare.builder()
                .userId(uID)
                .shareId(id).build());
        if (midUserShare!=null)
            return share;
        // TODO: 2021/9/23 2. 根据当前登录的用户id，查询积分是否充足
        
        UserDTO userDTO = this.userCenterFeignClient.findByID(uID);
        Integer price = share.getPrice();
        if (userDTO.getBonus()< price)
              throw new IllegalArgumentException("您的积分不够");

        // TODO: 2021/9/23 3.扣减积分
        UserDTO userDTO2 = userCenterFeignClient.addBonus(UserAddBonusDTO.builder()
                .bonus(-price)
                .userId(id).build());

        // TODO: 2021/9/23 4.并且往mid_user_share里面插入一条数据
        midUserShareMapper.insert(MidUserShare.builder()
                .shareId(share.getId())
                .userId(userDTO.getId()).build());
        return share;
    }
}
