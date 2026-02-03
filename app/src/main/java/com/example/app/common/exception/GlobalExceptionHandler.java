package com.example.app.common.exception;

import com.example.app.common.response.CustomResponse;
import com.example.core.exception.CustomException;
import com.example.core.exception.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger err = LoggerFactory.getLogger("ERROR_LOG");

    @ExceptionHandler(CustomException.class)
    protected ResponseEntity<CustomResponse<Object>> handleCustomException(CustomException e, HttpServletRequest request) {
        logWarn(request, e.getErrorCode(), e.getMessage(), e.getParams());
        if (!e.getParams().isEmpty()) {
            return CustomResponse.error(e.getErrorCode(), e.getParams());
        }
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
            logError(request, e);
            return CustomResponse.error(AppErrorCode.INVALID_INPUT_ERROR);
        }
        String msg = extractFieldErrorMessage(e);
        logWarn(request, AppErrorCode.INVALID_INPUT_ERROR, msg, Map.of());

        return CustomResponse.error(AppErrorCode.INVALID_INPUT_ERROR,msg);
    }

    // body 타입파싱 문제
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<CustomResponse<Object>> handleNotReadable(
            HttpMessageNotReadableException e, HttpServletRequest request) {
        logError(request, e);
        return CustomResponse.error(AppErrorCode.INVALID_INPUT_ERROR);
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<CustomResponse<Object>> handleException(
            Exception e, HttpServletRequest request) {
        logError(request, e);
        return CustomResponse.error(AppErrorCode.INTERNAL_SERVER_ERROR,e.getMessage());
    }

    private void logWarn(HttpServletRequest req, ErrorCode code, String message, Map<String, Object> params) {
        err.warn("api_failed method={}, path={}, code={}, status={}, coedMessage={}, serverErrorMessage={}, params={}",
                req.getMethod(), req.getRequestURI(), code.code(), code.status(), code.message(), message, params);
    }

    private void logError(HttpServletRequest req, Exception e) {
        err.error("api_failed method={}, path={}, serverErrorMessage={}",
                req.getMethod(), req.getRequestURI(), e.getMessage());
    }

    private String extractFieldErrorMessage(MethodArgumentNotValidException e) {
        FieldError fe = e.getBindingResult().getFieldError();
        if (fe == null) return "validation failed";
        return fe.getObjectName() + " " + fe.getField() + ": " + fe.getDefaultMessage();
    }
}
