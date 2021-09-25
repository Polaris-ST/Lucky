package com.itmuch.cententcenter.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RocketmqTransactionLog {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private String transactionID;
    private String log;

}
