package com.example.db.common.redis.service;

import com.example.app.api.path.port.cache.GraphCachePort;
import com.example.app.logging.event.AppLogEvent;
import com.example.app.logging.event.ErrorLogEvent;
import com.example.app.logging.logger.LogEventLogger;
import com.example.core.domain.path.Path;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

@Slf4j
@Component
public class GraphCacheAdapter implements GraphCachePort {

    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;
    private static final String GRAPH_KEY = "graph";
    private final static String GET_GRAPH_ERROR = "Get Graph Cache Error";
    private final static String SET_GRAPH_ERROR = "Set Graph Cache Error";

    public GraphCacheAdapter(
            StringRedisTemplate stringRedisTemplate,
            @Qualifier("redisObjectMapper") ObjectMapper objectMapper
    ) {
        this.redisTemplate = stringRedisTemplate;
        this.objectMapper = objectMapper;
    }

    @Override
    public Optional<Path> get() {
        String json = redisTemplate.opsForValue().get(GRAPH_KEY);
        if (json == null || json.isBlank()) {
            return Optional.empty();
        }
        try {
            return Optional.of(objectMapper.readValue(json, Path.class));
        } catch (Exception e) {
            Map<String, Object> params = Map.of("graph", "GET");
            cacheLog(GET_GRAPH_ERROR, params, e);
            redisTemplate.delete(GRAPH_KEY);
            return Optional.empty();
        }
    }

    @Override
    public void set(Path path) {
        try {
            String json = objectMapper.writeValueAsString(path);
            redisTemplate.opsForValue().set(GRAPH_KEY, json);
        } catch (Exception e) {
            Map<String, Object> params = Map.of("Path", "SET");
            cacheLog(SET_GRAPH_ERROR, params, e);
        }
    }

    @Override
    public void evict() {
        redisTemplate.delete(GRAPH_KEY);
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
