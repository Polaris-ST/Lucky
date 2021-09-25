package com.itmuch.usercenter.mapper;

import com.itmuch.usercenter.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 分享 Mapper 接口
 * </p>
 *
 * @author Polaris-ST
 * @since 2021-08-04
 */
@Repository
public interface UserMapper extends BaseMapper<User> {
    public User selectOne(String id);

    public User selectByEntity(User user);
}
