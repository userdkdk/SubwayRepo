package com.example.app.api.line.application;

import com.example.app.api.line.adapter.LineApiMapper;
import com.example.app.api.line.api.dto.response.LineResponse;
import com.example.app.api.station.adapter.StationApiMapper;
import com.example.app.api.station.api.dto.response.StationSegmentResponse;
import com.example.app.business.line.LineQueryRepository;
import com.example.app.business.segment.SegmentJpaEntity;
import com.example.app.business.segment.SegmentQueryRepository;
import com.example.app.common.exception.AppErrorCode;
import com.example.app.common.redis.service.RedisStationService;
import com.example.app.common.response.enums.StatusFilter;
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
public class LineViewService {

    private final LineQueryRepository lineQueryRepository;
    private final SegmentQueryRepository segmentQueryRepository;
    private final StationSorter stationSorter;
    private final StationApiMapper stationApiMapper;
    private final LineApiMapper lineApiMapper;
    private final RedisStationService redisStationService;
    private final ObjectMapper redisObjectMapper;

    public List<StationSegmentResponse> getStationsById(Integer lineId, StatusFilter status) {
        String cachedJson = redisStationService.getSegments(lineId, status);
        if (cachedJson != null && !cachedJson.isBlank()) {
            List<StationSegmentResponse> cached = tryReadList(cachedJson, lineId, status);
            if (cached != null) return cached;
        }

        if (!lineQueryRepository.existsActiveById(lineId)) {
            throw CustomException.domain(AppErrorCode.LINE_NOT_FOUND)
                    .addParam("line id", lineId);
        }
        List<SegmentJpaEntity> segments =
                segmentQueryRepository.findByLineAndActiveType(lineId, status);

        List<StationSegmentResponse> result = stationSorter.sortSegments(segments).stream()
                .map(stationApiMapper::segmentEntityToDto)
                .toList(); // 불변 OK (캐시값은 수정되면 안 됨)

        String json = writeJson(result);
        redisStationService.setSegments(lineId, status, json);

        return result;
    }
    // return line by activeType

    public List<LineResponse> getLines(StatusFilter status) {
        return lineQueryRepository.findByActiveType(status).stream()
                .map(lineApiMapper::entityToDto)
                .toList();
    }

    private List<StationSegmentResponse> tryReadList(String json, Integer lineId, StatusFilter status) {
        try {
            return redisObjectMapper.readValue(json, new TypeReference<>() {});
        } catch (Exception e) {
            redisStationService.evictSegments(lineId, status);
            // 캐시 깨졌으면 로그 남기고 캐시 삭제 후 폴백(운영 안정)
            log.warn("redis cache parse fail. evict. lineId={}, status={}", lineId, status, e);
            return null;
        }
    }

    private String writeJson(List<StationSegmentResponse> value) {
        try {
            return redisObjectMapper.writeValueAsString(value);
        } catch (Exception e) {
            throw CustomException.app(AppErrorCode.REDIS_SERIALIZATION_ERROR);
        }
    }
}
