package com.example.db.common.redis.service;

import com.example.app.api.line.port.cache.LineDetailCacheValue;
import com.example.app.common.dto.request.enums.StatusFilter;
import com.example.app.api.line.port.cache.LineDetailCachePort;
import com.example.app.logging.event.AppLogEvent;
import com.example.app.logging.event.ErrorLogEvent;
import com.example.app.logging.logger.LogEventLogger;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

@Slf4j
@Component
public class LineDetailCacheAdapter implements LineDetailCachePort {

    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;
    private final static String GET_LOG_ERROR = "Get Line Cache Error";
    private final static String SET_LOG_ERROR = "Set Line Cache Error";

    public LineDetailCacheAdapter(
            StringRedisTemplate stringRedisTemplate,
            @Qualifier("redisObjectMapper") ObjectMapper objectMapper
    ) {
        this.redisTemplate = stringRedisTemplate;
        this.objectMapper = objectMapper;
    }

    private String key(Integer lineId, StatusFilter status) {
        return "line:detail:%d:status:%s".formatted(lineId, status.name());
    }

    public Optional<LineDetailCacheValue> get(Integer lineId, StatusFilter status) {
        String key = key(lineId,status);
        String json = redisTemplate.opsForValue().get(key);
        if (json == null || json.isBlank()) {
            return Optional.empty();
        }

        try {
            return Optional.of(objectMapper.readValue(json, LineDetailCacheValue.class));
        } catch (Exception e) {
            Map<String, Object> params = Map.of("lineId", lineId);
            cacheLog(GET_LOG_ERROR, params, e);
            redisTemplate.delete(key);
            return Optional.empty();
        }
    }

    public void set(Integer lineId, StatusFilter status, LineDetailCacheValue value) {
        String key = key(lineId, status);
        try {
            String json = objectMapper.writeValueAsString(value);
            redisTemplate.opsForValue().set(key, json);
        } catch (Exception e) {
            Map<String, Object> params = Map.of("lineId", lineId);
            cacheLog(SET_LOG_ERROR, params, e);
        }
    }

    public void evict(Integer lineId) {
        for (StatusFilter s : StatusFilter.values()) {
            redisTemplate.delete(key(lineId, s));
        }
    }

    private void cacheLog(String message, Map<String, Object> params, Exception e) {
        AppLogEvent log = ErrorLogEvent.fromParams(
                message,
                params,
                e.getMessage()
        );
        LogEventLogger.log(log);
    }
}
