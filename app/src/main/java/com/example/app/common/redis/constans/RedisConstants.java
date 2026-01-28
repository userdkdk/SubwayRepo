package com.example.app.common.redis.constans;

import lombok.RequiredArgsConstructor;

import java.time.Duration;

@RequiredArgsConstructor
public class RedisConstants {
    public static final Duration LINE_CACHE_TTL = Duration.ofMinutes(10);
    public static final Duration PATH_CACHE_TTL = Duration.ofMinutes(60);
}
