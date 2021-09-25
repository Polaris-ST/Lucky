package com.itmuch.usercenter.auth;


import com.itmuch.usercenter.Utils.JwtOperator;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Objects;

@Component
@Aspect
@Slf4j
@RequiredArgsConstructor(onConstructor = @_(@Autowired))
public class CheckAllAspect {
    final private JwtOperator jwtOperator;
    @Around(value = "@annotation(com.itmuch.usercenter.auth.CheckLogin)")
    public Object checkLogin(ProceedingJoinPoint joinPoint) throws Throwable {

            checkToken();

        //执行下一方法
        return joinPoint.proceed();
    }

    public void checkToken() {
        try {
            //1从header里面获取token
            HttpServletRequest request = getHttpServletRequest();
            //上面是获取request;
            String token = request.getHeader("X-Token");
            //2校验token是否合法 如果不合法就抛异常
            jwtOperator.validateToken(token);

            //3如果校验信息成功 把用户信息放到attribute中
            Claims claimsFromToken = jwtOperator.getClaimsFromToken(token);
            request.setAttribute("id",claimsFromToken.get("id"));
            request.setAttribute("wxNickname",claimsFromToken.get("wxNickname"));
            request.setAttribute("role",claimsFromToken.get("role"));
        } catch (Exception e) {
            throw new SecurityException("Token非法");
        }
    }

    private HttpServletRequest getHttpServletRequest() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes attributes = (ServletRequestAttributes) requestAttributes;
        return attributes.getRequest();
    }

    @Around(value = "@annotation(com.itmuch.usercenter.auth.CheckAuthorization)")
    public Object CheckAuthorization(ProceedingJoinPoint joinPoint) throws Throwable {
        //验证Token是否合法
        checkToken();

        //获取用户角色
        try {
            String attribute = (String) getHttpServletRequest().getAttribute("role");
            //用aop的joinpoint获取被aop的类的
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            Method method = signature.getMethod();
            CheckAuthorization annotation = method.getAnnotation(CheckAuthorization.class);
            String value = annotation.value();
          if (!Objects.equals(value,attribute))
              throw new SecurityException("用户权限不匹配");
        } catch (SecurityException e) {
               throw new SecurityException("用户权限不匹配");
        }
        return joinPoint.proceed();



    }
}
