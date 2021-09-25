package com.itmuch.cententcenter.mapper;

import com.itmuch.cententcenter.entity.MidUserShare;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 用户-分享中间表【描述用户购买的分享】 Mapper 接口
 * </p>
 *
 * @author Polaris-ST
 * @since 2021-08-04
 */
public interface MidUserShareMapper extends BaseMapper<MidUserShare> {

    MidUserShare selectOneMidUserShare(MidUserShare share);
}
