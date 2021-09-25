package com.itmuch.cententcenter.Config;

import com.itmuch.cententcenter.feignClients.interceptor.TokenRelayRequestInterceptor;
import feign.Logger;
import feign.RequestInterceptor;
import org.omg.PortableInterceptor.RequestInfoOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.logging.Level;

@Component
public class GlobalFeignConfiguration {
    @Bean
    public Logger.Level level() {
        return Logger.Level.FULL;
    }
    @Bean
    public RequestInterceptor requestInterceptor()
    {
        return new TokenRelayRequestInterceptor();
    }
}
