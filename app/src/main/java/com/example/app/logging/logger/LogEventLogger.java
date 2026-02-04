package com.example.app.logging.logger;

import com.example.app.logging.event.AppLogEvent;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static net.logstash.logback.argument.StructuredArguments.kv;

@Slf4j
public class LogEventLogger {
    private static final Logger ACCESS =
            LoggerFactory.getLogger("ACCESS_LOG");

    private static final Logger ERROR =
            LoggerFactory.getLogger("ERROR_LOG");

    public static void log(AppLogEvent event) {
        switch (event.category()) {
            case ACCESS -> ACCESS.info(
                    event.message(),
                    kv("category", event.category().name()),
                    kv("fields", event.fields())
            );
            case ERROR -> ERROR.error(
                    event.message(),
                    kv("category", event.category().name()),
                    kv("fields", event.fields())
            );
        };
    }
}
