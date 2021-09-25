package com.itmuch.cententcenter.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itmuch.cententcenter.entity.RocketmqTransactionLog;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;


public interface RocketmqTransactionLogMapper extends BaseMapper<RocketmqTransactionLog> {

    RocketmqTransactionLog selectBytransactionID(String transactionID);

    void insertMy(RocketmqTransactionLog log);
}
