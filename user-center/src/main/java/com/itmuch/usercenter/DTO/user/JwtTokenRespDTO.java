package com.itmuch.usercenter.DTO.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JwtTokenRespDTO {
    /**
     * token
     */
    private String token;
    /**
     * token过期时间
     */
    private Long expirationTime;
}
