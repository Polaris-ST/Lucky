package com.itmuch.usercenter.DTO.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRespDTO {
    /**
     * 头像地址
     */
    private String avatarUrl;
    /**
     *积分
     */
    private Integer bonus;
    /**
     *用户id
     */
    private Integer id;
    /**
     *微信昵称
     */
    private String wxNickname;
}
