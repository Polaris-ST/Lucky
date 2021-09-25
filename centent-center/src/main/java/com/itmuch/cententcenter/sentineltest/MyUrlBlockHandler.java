package com.itmuch.cententcenter.sentineltest;

import com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.BlockExceptionHandler;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.authority.AuthorityException;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeException;
import com.alibaba.csp.sentinel.slots.block.flow.FlowException;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowException;
import com.alibaba.csp.sentinel.slots.system.SystemBlockException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Component
public class MyUrlBlockHandler implements BlockExceptionHandler {
    @Override
    public void handle(HttpServletRequest httpServletRequest,
                       HttpServletResponse httpServletResponse,
                       BlockException e) throws Exception {
        ErrorMsg msg = null;
        if (e instanceof ParamFlowException) {
            log.error("参数热点异常");
            msg = ErrorMsg.builder().status(100).msg("参数热点异常").build();
        }
        if (e instanceof FlowException) {
            log.error("限流异常");
            msg = ErrorMsg.builder().status(101).msg("限流异常").build();
        }
        if (e instanceof SystemBlockException) {
            log.error("系统规则异常");
            msg = ErrorMsg.builder().status(102).msg("系统规则异常").build();
        }
        if (e instanceof DegradeException) {
            log.error("降级异常");
            msg = ErrorMsg.builder().status(103).msg("降级异常").build();
        }
        if (e instanceof AuthorityException) {
            log.error("授权规则异常");
            msg = ErrorMsg.builder().status(104).msg("授权规则异常").build();
        }
        httpServletResponse.setStatus(500);
        httpServletResponse.setCharacterEncoding("utf-8");
        httpServletResponse.setHeader("Content-Type", "application/json;charset=utf-8");
        httpServletResponse.setContentType("application/json;charset=utf-8");
        // spring MVC的json工具
        new ObjectMapper().writeValue(httpServletResponse.getWriter(), msg);
    }
}

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
class ErrorMsg {
    private Integer status;
    private String msg;
}
