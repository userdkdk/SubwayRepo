package com.example.app.logging.event;

import java.util.Map;

public record AccessLogEvent(
        String message,
        String method,
        String requestURI,
        int status,
        long elapsedTime
) implements AppLogEvent{
    @Override
    public LogCategory category() {
        return LogCategory.ACCESS;
    }

    @Override
    public Map<String, Object> fields() {
        return Map.of(
                "method", method,
                "path", requestURI,
                "status", status,
                "elapsedMs", elapsedTime
        );
    }
}
