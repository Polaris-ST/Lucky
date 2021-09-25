package com.itmuch.usercenter.DTO.messaging;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserAddBonusMsgDTO {
    /*
    为谁加积分
     */
    private Integer userID;
    /*
    加多少积分
     */
    private Integer bonus;
    private String description;
    private String event;
}
