package com.example.app.logging.event;

import java.util.Map;

public interface AppLogEvent {
    String message();
    LogCategory category();
    Map<String, Object> fields();
}
