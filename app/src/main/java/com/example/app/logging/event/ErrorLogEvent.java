package com.example.app.logging.event;

import com.example.core.exception.ErrorCode;

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
}
