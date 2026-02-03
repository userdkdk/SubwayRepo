package com.example.app.logging.property;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.logging")
public record LogProperty (
        boolean enabled,
        boolean accessLogEnabled
){}
