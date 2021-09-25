package com.itmuch.usercenter.RocketMQ;

import com.itmuch.usercenter.DTO.messaging.UserAddBonusMsgDTO;
import com.itmuch.usercenter.entity.BonusEventLog;
import com.itmuch.usercenter.entity.User;
import com.itmuch.usercenter.mapper.BonusEventLogMapper;
import com.itmuch.usercenter.mapper.UserMapper;
import com.itmuch.usercenter.service.impl.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor(onConstructor = @_(@Autowired))
public class AddBounsListener {

    private final UserMapper userMapper;
    private final UserServiceImpl userService;

    @StreamListener(Sink.INPUT)
    public void receive(UserAddBonusMsgDTO msgDTO) {
        msgDTO.setDescription("CONTRIBUTE");
        msgDTO.setEvent("投稿加积分");
        userService.addBonus(msgDTO);
    }

}
