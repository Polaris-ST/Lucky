package com.itmuch.usercenter.DTO.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginRespDTO {
    /**
     * token
     */
    private JwtTokenRespDTO token;
    /**
     *  用户信息
     */
    private UserRespDTO user;
}
