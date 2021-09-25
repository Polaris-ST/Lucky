package com.itmuch.cententcenter.rocketMQ;

import com.alibaba.fastjson.JSON;
import com.itmuch.cententcenter.DTO.Centent.ShareAuditDTO;
import com.itmuch.cententcenter.entity.RocketmqTransactionLog;
import com.itmuch.cententcenter.mapper.RocketmqTransactionLogMapper;
import com.itmuch.cententcenter.service.impl.ShareServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.apache.rocketmq.spring.annotation.RocketMQTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionState;
import org.apache.rocketmq.spring.support.RocketMQHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.transaction.annotation.Transactional;

@Transactional(rollbackFor = Exception.class)
@RocketMQTransactionListener(txProducerGroup = "tx-add-bonus-group")
public class AddBounsTransctionListener implements RocketMQLocalTransactionListener {
    @Autowired
    ShareServiceImpl shareService;

    @Override
    //执行第三步 四步骤 本地事务成功 提交 失败回滚
    public RocketMQLocalTransactionState executeLocalTransaction(Message message, Object arg) {
        MessageHeaders headers = message.getHeaders();
        String transactionID = (String) headers.get(RocketMQHeaders.TRANSACTION_ID);
        Integer shareID = Integer.valueOf((String) headers.get("share_id"));
        String dto = (String) headers.get("dto");
        ShareAuditDTO shareAuditDTO = JSON.parseObject(dto, ShareAuditDTO.class);
        try {
            shareService.auditByIdWithRocketMqLog(shareAuditDTO, shareID, transactionID);
            return RocketMQLocalTransactionState.COMMIT;
        } catch (Exception e) {
            e.printStackTrace();
            return RocketMQLocalTransactionState.ROLLBACK;
        }
    }

    //执行第五步 六 七 回查
    @Autowired(required = false)
    RocketmqTransactionLogMapper rocketmqTransactionLogMapper;

    @Override
    public RocketMQLocalTransactionState checkLocalTransaction(Message message) {
        MessageHeaders headers = message.getHeaders();
        String transactionID = (String) headers.get(RocketMQHeaders.TRANSACTION_ID);
        RocketmqTransactionLog rocketmqTransactionLog =
                rocketmqTransactionLogMapper.selectBytransactionID(transactionID);
        if (rocketmqTransactionLog != null) {
            return RocketMQLocalTransactionState.COMMIT;
        } else
            return RocketMQLocalTransactionState.ROLLBACK;

    }
}
