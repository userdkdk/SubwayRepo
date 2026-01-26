package com.example.app.common.redis.constans;

import lombok.RequiredArgsConstructor;

import java.time.Duration;

@RequiredArgsConstructor
public class RedisConstants {
    public static final Duration CACHE_TTL = Duration.ofMinutes(10);
}
