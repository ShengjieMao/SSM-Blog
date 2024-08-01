package com.sj.handler.exception;

import com.sj.domain.ResponseResult;
import com.sj.enums.AppHttpCodeEnum;
import com.sj.exception.SystemException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(SystemException.class)
    public ResponseResult systemExceptionHandler(SystemException e){
        return ResponseResult.errorResult(e.getCode(),e.getMsg());

    }

    @ExceptionHandler(Exception.class)
    public ResponseResult exceptionHandler(Exception e){
        // Dev: detailed message
        return ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR.getCode(),e.getMessage());
    }
}