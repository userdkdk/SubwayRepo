package com.example.app.common.exception;

import com.example.app.common.response.CustomResponse;
import com.example.app.logging.event.AppLogEvent;
import com.example.app.logging.event.ErrorLogEvent;
import com.example.app.logging.logger.LogEventLogger;
import com.example.core.exception.CustomException;
import com.example.core.exception.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    protected ResponseEntity<CustomResponse<Object>> handleCustomException(CustomException e, HttpServletRequest request) {
        logWarn(request, e.getErrorCode(), e.getParams(), e);
        if (!e.getParams().isEmpty()) {
            return CustomResponse.error(e.getErrorCode(), e.getParams());
        }
        return CustomResponse.error(e.getErrorCode());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<CustomResponse<Object>> handleDataIntegrity(DataIntegrityViolationException e, HttpServletRequest request) {
        logError(request,e);
        return CustomResponse.error(AppErrorCode.DATA_INTEGRITY_VIOLATION, e.getMessage());
    }

    // valid 검증 실패
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<CustomResponse<Object>> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException e, HttpServletRequest request) {
        Map<String, Object> msg = extractFieldErrorMessage(e);
        logWarn(request, AppErrorCode.INVALID_INPUT_ERROR, msg, e);
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

    private void logWarn(HttpServletRequest req, ErrorCode code, Map<String, Object> params, Exception e) {
        AppLogEvent log = new ErrorLogEvent(
                "ERROR LOG",
                req.getMethod(),
                req.getRequestURI(),
                code,
                params,
                e.getMessage()
        );
        LogEventLogger.log(log);
    }

    private void logError(HttpServletRequest req, Exception e) {
        AppLogEvent log = new ErrorLogEvent(
                "ERROR LOG",
                req.getMethod(),
                req.getRequestURI(),
                null,
                null,
                e.getMessage()
        );
        LogEventLogger.log(log);
    }

    private Map<String, Object> extractFieldErrorMessage(MethodArgumentNotValidException e) {
        FieldError fe = e.getBindingResult().getFieldError();
        Map<String, Object> map = new HashMap<>();
        if (fe == null) return map;
        map.put("objectName",fe.getObjectName());
        map.put("field",fe.getField());
        map.put("message",fe.getDefaultMessage());
        return map;
    }
}
