package com.example.app.common.redis.service;

import com.example.app.common.redis.constans.RedisConstants;
import com.example.app.common.response.enums.StatusFilter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class RedisSegmentService {

    private final StringRedisTemplate redis;
    private static final String PATH_KEY = "path";

    public String getPath() {
        return redis.opsForValue().get(PATH_KEY);
    }

    public void setPath(String json) {
        redis.opsForValue().set(PATH_KEY,json, RedisConstants.PATH_CACHE_TTL);
    }

    public void evictPath() {
        redis.delete(PATH_KEY);
    }
}
