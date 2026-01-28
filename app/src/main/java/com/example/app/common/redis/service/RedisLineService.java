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
public class RedisLineService {

    private final StringRedisTemplate redis;

    private String cacheKey(Integer lineId, StatusFilter status) {
        return "segments:" + lineId + ":" + status.name();
    }

    public String getSegments(Integer lineId, StatusFilter status) {
        String key = cacheKey(lineId,status);
        return redis.opsForValue().get(key);
    }

    public void setSegments(Integer lineId, StatusFilter status, String json) {
        String key = cacheKey(lineId,status);
        redis.opsForValue().set(key,json, RedisConstants.LINE_CACHE_TTL);
    }

    public void evictSegments(Integer lineId, StatusFilter status) {
        redis.delete(cacheKey(lineId, status));
    }
}
