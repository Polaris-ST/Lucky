package com.itmuch.cententcenter.sentineltest.fallback;

import org.springframework.stereotype.Component;

@Component
public class MyFallBack {
    public String fallback() {
        return "error";
    }
}
