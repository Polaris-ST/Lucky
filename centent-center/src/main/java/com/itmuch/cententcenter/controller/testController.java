package com.itmuch.cententcenter.controller;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.Tracer;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.context.Context;
import com.alibaba.csp.sentinel.context.ContextUtil;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.nacos.common.utils.StringUtils;
import com.itmuch.cententcenter.Config.RestTemolate;
import com.itmuch.cententcenter.DTO.User.UserDTO;
import com.itmuch.cententcenter.feignClients.BaiDuFeignClient;
import com.itmuch.cententcenter.service.impl.UserTestServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
@RefreshScope
public class testController {
    @Autowired
    DiscoveryClient discoveryClient;
    @Autowired
    UserTestServiceImpl userTestService;

//    @Value("${myconfig.name}")
//    String name;
    @GetMapping("/nacos-configuration")
    public String myConfiguration()
    {
        return null;
    }
    @GetMapping("/test1")
    public List<ServiceInstance> getinser() {//              通过discoveryClient的api方法获得user-centent的实例信息
        return discoveryClient.getInstances("user-center");
    }

    @GetMapping("/test2")
    @SentinelResource("myTest")
    public UserDTO userDTO(UserDTO userDTO) {
        return userTestService.userDTO(userDTO);
    }

    @Autowired
    BaiDuFeignClient baiDuFeignClient;

    @GetMapping("/baidu")
    public String baiduIndex() {
        return baiDuFeignClient.index();
    }

    @GetMapping("/sentinel")
    public String Sentinel(@RequestParam(required = false) String a) {
        log.info("-------->>>>>>>>>");
        Entry entry = null;
        String resourceName = "test-sentinel-api";  //资源名称
        String orgName = "test--2fw";  //定义来源   ----------针对指定来源
        Context enter = ContextUtil.enter(resourceName, orgName);
        try {
            //创建一个sentinel保密的资源 名称是下面的
            entry = SphU.entry(resourceName);
            log.info("-------->>>>>>>>>");
            //被保护的业务逻辑
            if (StringUtils.isBlank(a))
                throw new IllegalArgumentException("a不能为空");
            return "hello";
        }
        //如果保护的资源被限流或者降级或者就会抛出异常
        catch (BlockException e) {
            log.info("-------->>>>>>>>>");
            log.warn("被降级了，或者限流了", e);
            return "被降级了，或者限流了\n" + e;
        } catch (IllegalArgumentException e2) {   //表示 e2异常也参与江流等异常处理
            Tracer.trace(e2);
            return "非法参数";
        } finally {
            if (entry != null)
                entry.exit();
            if (enter != null)
                ContextUtil.exit();
        }
    }

    /*
    将API的定义资源和限流处理分开成各种方法并且自动释放资源，讲其他异常自动进行限流
    避免了Tracer.trace(e2); 
     */
    @GetMapping("/sentinelResource")
    @SentinelResource(value = "test-sentinel-api",
            blockHandler = "block",
            fallback = "fallback")
    public String SentinelResource(@RequestParam(required = false) String a) {
        log.info("-------->>>>>>>>>");
        if (StringUtils.isBlank(a)) {
            throw new IllegalArgumentException("a不能为空");
        }
        return "hello";
    }

    /*
    处理限流或者降级
    参数和返回值和 指定SentinelResource一样
     */
    public String block(@RequestParam(required = false) String a, BlockException e) {
        log.warn(e + "被限流了");
        return "被限流了";
    }

    /*
    降级专用 1.6后可用Throwable
     */
    public String fallback(@RequestParam(required = false) String a, Throwable throwable) {
        throwable.printStackTrace();
        return "被降级了";
    }

    @Autowired
    private RestTemolate restTemolate;

    @GetMapping("Sentinal-Template/{id}")
    public UserDTO SentinalRestTemplate(@PathVariable("id") Integer id) {
        UserDTO forObject = restTemolate.restTemplate().getForObject(
                "http://user-center/users/{id}"
                , UserDTO.class,
                id);
        return forObject;
    }

    @Autowired(required = false)
    private Source source;

    @GetMapping("/stream-test")
    public String mQStreamTest() {
        source.output().send(
                MessageBuilder.withPayload("消息体").build()
        );
        return "successful";
    }

}
