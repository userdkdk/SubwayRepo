package com.example.app.api.path.application;

import com.example.app.api.path.api.dto.response.PathResponse;
import com.example.app.api.path.api.enums.PathFilter;
import com.example.app.api.segment.port.SegmentQueryPort;
import com.example.app.common.redis.port.RedisSegmentPort;
import com.example.core.domain.path.port.data.PathSegmentData;
import com.example.core.domain.path.PathCalculator;
import com.example.core.domain.path.Path;
import com.example.core.domain.path.PathResult;
import com.example.app.common.exception.AppErrorCode;
import com.example.core.common.exception.CustomException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PathService {

    private final RedisSegmentPort redisSegmentPort;
    private final ObjectMapper redisObjectMapper;
    private final SegmentQueryPort segmentQueryPort;

    public PathResponse getPath(Integer startId, Integer endId, PathFilter pathFilter) {
        String cachedJson = redisSegmentPort.getPath();
        Path path;
        log.info("-------------------{}", cachedJson);
        if (cachedJson != null && !cachedJson.isBlank()) {
            path = tryReadList(cachedJson);
        } else {
            List<PathSegmentData> segments = segmentQueryPort.findAllActive();
            path = Path.from(segments);
            String json = writeJson(path);
            redisSegmentPort.setPath(json);
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
            redisSegmentPort.evictPath();
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
