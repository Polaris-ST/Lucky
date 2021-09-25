package com.itmuch.cententcenter.Config;

import feign.Logger;
import org.springframework.context.annotation.Bean;

public class UserCenterFeignConfiguration {
    @Bean
    public Logger.Level level() {
        //显示所有细节
        return Logger.Level.FULL;
    }
}
