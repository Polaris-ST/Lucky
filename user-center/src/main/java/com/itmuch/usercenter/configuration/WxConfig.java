package com.itmuch.usercenter.configuration;

import me.chanjar.weixin.miniapp.api.WxMaService;
import me.chanjar.weixin.miniapp.api.impl.WxMaServiceImpl;
import me.chanjar.weixin.miniapp.config.WxMaConfig;
import me.chanjar.weixin.miniapp.config.impl.WxMaDefaultConfigImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WxConfig {
    @Bean
    public WxMaConfig wxMaConfig()
    {
        WxMaDefaultConfigImpl config = new WxMaDefaultConfigImpl();
        config.setAppid("wxab209007fdf99b5a");  //配置小程序开发的appid
        config.setSecret("d451f07d9140ed600a848b1adcecc542");  //密匙
        return config;
    }
    @Bean
    public WxMaService wxMaService()
    {
        WxMaServiceImpl service = new WxMaServiceImpl();
        service.setWxMaConfig(wxMaConfig());
        return service;
    }

}
