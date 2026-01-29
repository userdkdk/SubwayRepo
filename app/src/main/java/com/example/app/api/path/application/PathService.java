package com.example.app.api.path.application;

import com.example.app.api.path.api.dto.response.PathResponse;
import com.example.app.api.path.api.enums.PathFilter;
import com.example.app.business.path.PathBuilder;
import com.example.app.business.path.PathCalculator;
import com.example.app.business.path.model.Path;
import com.example.app.business.path.model.PathResult;
import com.example.app.common.exception.AppErrorCode;
import com.example.app.common.redis.service.RedisSegmentService;
import com.example.core.common.exception.CustomException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PathService {

    private final RedisSegmentService redisSegmentService;
    private final ObjectMapper redisObjectMapper;
    private final PathBuilder pathBuilder;

    public PathResponse getPath(Integer startId, Integer endId, PathFilter pathFilter) {
        String cachedJson = redisSegmentService.getPath();
        Path path;
        log.info("-------------------{}", cachedJson);
        if (cachedJson != null && !cachedJson.isBlank()) {
            path = tryReadList(cachedJson);
        } else {
            path = pathBuilder.build();
            String json = writeJson(path);
            redisSegmentService.setPath(json);
        }

        // calculate
        if (pathFilter==PathFilter.TIME) {
            PathResult res = PathCalculator.findByTime(path, startId, endId);
            return PathResponse.from(res);
        }

        return null;
    }

    private Path tryReadList(String json) {
        try {
            return redisObjectMapper.readValue(json, new TypeReference<Path>() {});
        } catch (Exception e){
            redisSegmentService.evictPath();
            log.warn("redis cache parse fail", e);
            return null;
        }
    }

    private String writeJson(Path value) {
        try {
            return redisObjectMapper.writeValueAsString(value);
        } catch (Exception e) {
            throw CustomException.app(AppErrorCode.REDIS_SERIALIZATION_ERROR);
        }
    }
}
