package com.example.app.logging.event;

import com.example.core.common.exception.ErrorCode;

import java.util.Map;

public record ErrorLogEvent(
        String message,
        String method,
        String requestURI,
        ErrorCode code,
        Map<String, Object> params,
        String logMessage
) implements AppLogEvent {

    @Override
    public LogCategory category() {
        return LogCategory.ERROR;
    }

    @Override
    public Map<String, Object> fields() {
        return Map.of(
                "method", method,
                "path", requestURI,
                "status", code.status(),
                "errorType", code.errorType(),
                "errorCode", code.code(),
                "errorMessage",code.message(),
                "errorDetail", params,
                "logMessage",logMessage
        );
    }
    public static ErrorLogEvent from(String message, String method, String uri,
                                ErrorCode code, Map<String, Object> params, String logMessage) {
        return new ErrorLogEvent(message, method, uri,
                code, params, logMessage);
    }
    public static ErrorLogEvent fromWithoutCode(String message, String method, String uri,
                                     String logMessage) {
        return new ErrorLogEvent(message, method, uri,
                null, null, logMessage);
    }
    public static ErrorLogEvent fromParams(String message, Map<String, Object> params,
                                                String logMessage) {
        return new ErrorLogEvent(message, null,null,
                null, params, logMessage);
    }
}
