package com.itmuch.usercenter.DTO.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserLoginDTO {
    /**
     *  code
     */
    private String code;
    /**
     *  头像地址
     */
    private String avatarUrl;
    /**
     * 微信名称
     */
    private String wxNickname;
}
