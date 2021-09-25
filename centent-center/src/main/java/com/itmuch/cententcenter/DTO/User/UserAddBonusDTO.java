package com.itmuch.cententcenter.DTO.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserAddBonusDTO {
    /**
     * 用户id
     */
    private Integer userId;
    /**
     * 积分
     */
    private Integer bonus;
}
