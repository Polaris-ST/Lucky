package com.itmuch.cententcenter.feignClients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "baidu", url = "https://www.imooc.com/article/289000")
public interface BaiDuFeignClient {
    @GetMapping("")
    String index();
}
