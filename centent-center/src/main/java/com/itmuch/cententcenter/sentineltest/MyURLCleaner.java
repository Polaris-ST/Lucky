package com.itmuch.cententcenter.sentineltest;

import com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.UrlCleaner;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.stereotype.Component;

import java.util.Arrays;

//@Component
//已经实现了内部
@Slf4j
public class MyURLCleaner implements UrlCleaner {
    @Override
    public String clean(String s) {
        log.error(s);
        String[] split = s.split("/");
        String url = Arrays.stream(split).map(string -> {
            if (string.equals("{id}"))
                return "{number}";
            else
                return string;
        }).reduce((a, b) -> a + "/" + b).orElse("error");
        log.info(url);
        return null;
    }
}
