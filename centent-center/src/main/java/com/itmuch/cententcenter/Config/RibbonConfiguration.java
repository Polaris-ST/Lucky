package com.itmuch.cententcenter.Config;

import RConfiguration.RibbonConfig;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.netflix.ribbon.RibbonClients;
import org.springframework.context.annotation.Configuration;


@Configuration
@RibbonClients(defaultConfiguration = RibbonConfig.class)
public class RibbonConfiguration {
}
