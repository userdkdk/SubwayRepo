package com.example.app.logging.event;

import com.example.core.exception.ErrorCode;
import com.example.core.exception.ErrorType;

import java.time.LocalDateTime;
import java.util.Map;

public record LogEvent(
        LogCategory category,
        String traceId,
        String message,
        LocalDateTime timeStamp,
        Map<String, Object> fields,
        ErrorCode errorCode,
        ErrorType errorType
) {
    public static LogEvent info(LogCategory category, String traceId,
                                String message, Map<String, Object> fields) {
        return new LogEvent(category, traceId, message, LocalDateTime.now(),
                fields,  null, null);
    }
    public static LogEvent error(LogCategory category, String traceId,
                                 String message, Map<String, Object> fields,
                                 ErrorCode code) {
        return new LogEvent(category, traceId, message, LocalDateTime.now(),
                fields,  code, code.errorType());
    }
}
