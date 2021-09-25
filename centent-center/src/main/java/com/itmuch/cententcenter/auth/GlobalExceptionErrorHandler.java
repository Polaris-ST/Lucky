package com.itmuch.cententcenter.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

//MVC的异常统一管理
@RestControllerAdvice
@Slf4j
public class GlobalExceptionErrorHandler {
    @ExceptionHandler(SecurityException.class)
    public ResponseEntity<ErrorBody> error(SecurityException e)
    {   log.warn("发生了(SecurityException错误");
        return new ResponseEntity<>(
                ErrorBody.builder().body(e.getMessage())
                        .status(HttpStatus.UNAUTHORIZED.value()).build()
                ,HttpStatus.UNAUTHORIZED
        );
    }
}

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
class ErrorBody
{
    private int status;
    private String body;
}
