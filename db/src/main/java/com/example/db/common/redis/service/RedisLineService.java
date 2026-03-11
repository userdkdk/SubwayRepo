package com.example.db.common.redis.service;

import com.example.app.common.dto.request.enums.StatusFilter;
import com.example.app.common.redis.port.RedisLinePort;
import com.example.db.common.redis.constans.RedisConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class RedisLineService implements RedisLinePort {

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

    public void evictSegments(Integer lineId) {
        for (StatusFilter s : StatusFilter.values()) {
            redis.delete(cacheKey(lineId, s));
        }
    }
}
