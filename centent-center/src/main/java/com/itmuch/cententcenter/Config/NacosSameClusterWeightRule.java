package com.itmuch.cententcenter.Config;

import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.alibaba.cloud.nacos.NacosServiceInstance;
import com.alibaba.cloud.nacos.discovery.NacosDiscoveryClient;
import com.alibaba.cloud.nacos.ribbon.NacosServer;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.alibaba.nacos.client.naming.core.Balancer;
import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.AbstractLoadBalancerRule;
import com.netflix.loadbalancer.BaseLoadBalancer;
import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.Server;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class NacosSameClusterWeightRule extends AbstractLoadBalancerRule {
    @Autowired
    NacosDiscoveryProperties properties;

    @Override
    public void initWithNiwsConfig(IClientConfig iClientConfig) {
    }

    @Override
    public Server choose(Object o) {
        try {
            //获取集群名称
            String clusterName = properties.getClusterName();
            //Ribbon入口
            BaseLoadBalancer loadBalancer = (BaseLoadBalancer) this.getLoadBalancer();
            //获取请求服务名称
            String name = loadBalancer.getName();
            //获取nacosApi
            NamingService namingService = properties.namingServiceInstance();
            //找到nacos中的全部实例
            List<Instance> instances = namingService.selectInstances(name, true);
            //过滤出nacos中的相同集群的实例
            List<Instance> sameCusterNameInstances = instances.stream()
                    .filter(instance -> instance.getClusterName().equals(clusterName))
                    .collect(Collectors.toList());
            //如果集群中没有则从nacos中调用
            List<Instance> targetInstance;
            if (CollectionUtils.isEmpty(sameCusterNameInstances)) {
                targetInstance = instances;
            } else
                targetInstance = sameCusterNameInstances;
            //通过包装类来实现负载均衡当发的调用
            Instance instance = MyBalancer.myGetHostByRandomWeight(targetInstance);
            //  返回权重负载后的实例
            return new NacosServer(instance);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

//通过自己写的类 继承一下来把使用权限不够大方法
class MyBalancer extends Balancer {
    public static Instance myGetHostByRandomWeight(List<Instance> hosts) {

        return Balancer.getHostByRandomWeight(hosts);
    }
}
