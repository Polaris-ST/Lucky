package com.itmuch.cententcenter.feignClients.interceptor;

import com.alibaba.nacos.common.utils.StringUtils;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
@Slf4j
//实现RequestInterceptor做feginclient的过滤器
public class TokenRelayRequestInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate template) {
      //获取token
        //1从header里面获取token
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes attributes = (ServletRequestAttributes) requestAttributes;
        HttpServletRequest request = attributes.getRequest();
        //上面是获取request;
        String token = request.getHeader("X-Token");
        log.info("tokennnnn={}",token);
        //传递token
        if (StringUtils.isNotBlank(token))
            template.header("X-Token",token);
    }
}
