package com.example.app.common.exception;

import com.example.app.common.response.CustomResponse;
import com.example.core.common.exception.CustomException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Arrays;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    protected ResponseEntity<CustomResponse<Object>> handleCustomException(CustomException e, HttpServletRequest request) {
        if (!e.getParams().isEmpty()) {
            return CustomResponse.error(e.getErrorCode(), e.getParams());
        }
        log.error("[Custom Exception] {} {} {}",
                request.getMethod(),
                request.getRequestURI(),
                e.getMessage());
        return CustomResponse.error(e.getErrorCode());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<CustomResponse<Object>> handleDataIntegrity(DataIntegrityViolationException e) {
        log.error("[DataIntegrityViolationException] {}",
                e.getMessage());
        return CustomResponse.error(AppErrorCode.DATA_INTEGRITY_VIOLATION, e.getMessage());
    }

    // valid 검증 실패
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<CustomResponse<Object>> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException e, HttpServletRequest request) {
        FieldError fieldError = e.getBindingResult().getFieldError();
        if (fieldError == null) {
            log.error("[ValidationException] {} {}: 유효성 검증 에러",
                    request.getMethod(),
                    request.getRequestURI()
            );
            return CustomResponse.error(AppErrorCode.INVALID_INPUT_ERROR);
        }
        String object = fieldError.getObjectName();
        String field = fieldError.getField();
        String message = fieldError.getDefaultMessage();
        log.error("[ValidationException] {} {} {} {} {}",
                request.getMethod(),
                request.getRequestURI(),
                object,
                field,
                message
        );
        return CustomResponse.error(AppErrorCode.INVALID_INPUT_ERROR,
                object +" "+field+": "+message);
    }

    // body 타입파싱 문제
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<CustomResponse<Object>> handleNotReadable(
            HttpMessageNotReadableException e, HttpServletRequest request) {
        log.error("[ValidationException] {} {} : {}",
                request.getMethod(),
                request.getRequestURI(),
                e.getLocalizedMessage()
        );
        return CustomResponse.error(AppErrorCode.INVALID_INPUT_ERROR);
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<CustomResponse<Object>> handleException(
            Exception e, HttpServletRequest request) {
        log.error("[UnhandledException] {} {} {}",
                request.getMethod(),
                request.getRequestURI(),
                e.getMessage());
        return CustomResponse.error(AppErrorCode.INTERNAL_SERVER_ERROR,e.getMessage());
    }
}
