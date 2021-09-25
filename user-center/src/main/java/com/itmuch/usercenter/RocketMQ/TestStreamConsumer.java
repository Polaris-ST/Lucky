package com.itmuch.usercenter.RocketMQ;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TestStreamConsumer {
    @StreamListener(Sink.INPUT)
    public void receive(String mesBody) {
        System.out.println(mesBody);
        log.info("收到了消息={}", mesBody);
    }

}
