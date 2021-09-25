package com.itmuch.usercenter.service.impl;

import com.itmuch.usercenter.DTO.messaging.UserAddBonusMsgDTO;
import com.itmuch.usercenter.DTO.user.UserLoginDTO;
import com.itmuch.usercenter.entity.BonusEventLog;
import com.itmuch.usercenter.entity.User;
import com.itmuch.usercenter.mapper.BonusEventLogMapper;
import com.itmuch.usercenter.mapper.UserMapper;
import com.itmuch.usercenter.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.annotations.Param;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Wrapper;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * <p>
 * 分享 服务实现类
 * </p>
 *
 * @author Polaris-ST
 * @since 2021-08-04
 */
@Service
@RequiredArgsConstructor(onConstructor = @_(@Autowired))
@Transactional(rollbackFor = Exception.class)
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {


    private final UserMapper userMapper;
    private final BonusEventLogMapper bonusEventLogMapper;

    //将业务类写入serice
    public void addBonus(UserAddBonusMsgDTO userAddBonusMsgDTO) {
        //当收到消息时 执行业务
//        1.为用户加积分
        Integer userID = userAddBonusMsgDTO.getUserID();
        Integer bonus = userAddBonusMsgDTO.getBonus();
        User user = userMapper.selectById(userID);
        user.setBonus(user.getBonus() + bonus);
        userMapper.updateById(user);

//        2.加到bonus_event_log表里面
        bonusEventLogMapper.insert(BonusEventLog.builder()
                .userId(userID)
                .value(bonus)
                .event(userAddBonusMsgDTO.getEvent())
                .createTime(new Date())
                .description(userAddBonusMsgDTO.getDescription())
                .build());
    }
    public User login(UserLoginDTO loginDTO,String openid)
    {
        User user = userMapper.selectByEntity(User.builder().wxId(openid).build());

        if (user==null)
        {
            User userToSave = User.builder().wxId(openid)
                    .bonus(300)
                    .roles("user")
                    .avatarUrl(loginDTO.getAvatarUrl())
                    .wxNickname(loginDTO.getWxNickname())
                    .createTime(new Date())
                    .updateTime(new Date())
                    .build();
            userMapper.insert(userToSave);
        }
        return user;
    }
}
