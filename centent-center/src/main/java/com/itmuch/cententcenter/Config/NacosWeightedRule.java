package com.itmuch.cententcenter.Config;

import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.alibaba.cloud.nacos.ribbon.NacosServer;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

//继承Ribbon官方负载均衡规则自定义类
@Slf4j
public class NacosWeightedRule extends AbstractLoadBalancerRule {
    @Autowired
    private NacosDiscoveryProperties properties;   //注入Nacos实例的配置管理器

    @Override
    public void initWithNiwsConfig(IClientConfig iClientConfig) {

    }

    @Override
    public Server choose(Object o) {
        try {
            //获得Ribbon的入口
            BaseLoadBalancer loadBalancer = (BaseLoadBalancer) this.getLoadBalancer();
            //获取请求的服务名称
            String name = loadBalancer.getName();
            log.info(name);
            //拿到服务发现的相关api
            NamingService namingService = properties.namingServiceInstance();
            //naocs客户端基于权重负载均衡算法给我们选择一个健康的实例
            Instance instance = namingService.selectOneHealthyInstance(name, true);
            log.info(String.valueOf(instance.getPort()), instance);
            //返回实例
            return new NacosServer(instance);

        } catch (NacosException e) {
            log.info("负载均衡出错了");
            return null;
        }
    }
}
