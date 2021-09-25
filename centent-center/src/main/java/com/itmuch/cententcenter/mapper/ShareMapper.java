package com.itmuch.cententcenter.mapper;

import com.itmuch.cententcenter.entity.Share;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 分享表 Mapper 接口
 * </p>
 *
 * @author Polaris-ST
 * @since 2021-08-04
 */
@Repository
public interface ShareMapper extends BaseMapper<Share> {
    int updateByIdSelective(Share share);

    List<Share> selectByParam(@Param("title") String title);

//    int updateByIdSelective(Share share);
}
