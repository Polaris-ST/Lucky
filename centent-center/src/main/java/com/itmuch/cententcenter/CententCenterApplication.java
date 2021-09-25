package com.itmuch.cententcenter;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;

@MapperScan("com.itmuch.cententcenter.mapper")
@SpringBootApplication
@EnableCaching
@EnableFeignClients   //开启Fegin
@EnableBinding({Source.class})
public class CententCenterApplication {

    public static void main(String[] args) {
        SpringApplication.run(CententCenterApplication.class, args);
    }

}
