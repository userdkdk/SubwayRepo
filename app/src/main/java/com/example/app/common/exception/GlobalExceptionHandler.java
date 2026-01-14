package com.example.app.common.exception;

import com.example.app.common.response.CustomResponse;
import com.example.core.common.exception.CustomException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    protected ResponseEntity<CustomResponse<Object>> handleCustomException(CustomException e, HttpServletRequest request) {
        if (!e.getParams().isEmpty()) {
            return CustomResponse.error(e.getErrorCode(), e.getParams());
        }
        return CustomResponse.error(e.getErrorCode());
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<CustomResponse<Object>> handleException(
            Exception e, HttpServletRequest request) {
        return CustomResponse.error(AppErrorCode.INTERNAL_SERVER_ERROR,e.getMessage());
    }
}
