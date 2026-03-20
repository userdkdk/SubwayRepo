package com.example.app.api.line.application;

import com.example.app.api.line.api.dto.response.LineDetailResponse;
import com.example.app.api.line.api.dto.response.LineResponse;
import com.example.app.api.line.port.LineQueryPort;
import com.example.app.api.line.port.row.LineProjection;
import com.example.app.api.line.port.row.LineSegmentRow;
import com.example.app.api.segment.port.SegmentQueryPort;
import com.example.app.api.station.api.dto.response.StationSegmentResponse;
import com.example.app.common.dto.page.PageResult;
import com.example.app.common.dto.request.ViewRequestFilter;
import com.example.app.common.dto.page.CustomPage;
import com.example.app.common.dto.request.enums.StatusFilter;
import com.example.app.common.exception.AppErrorCode;
import com.example.app.common.redis.port.RedisLinePort;
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

    private final LineQueryPort lineQueryPort;
    private final SegmentQueryPort segmentQueryPort;
    private final StationSorter stationSorter;
    private final RedisLinePort redisLinePort;
    private final ObjectMapper redisObjectMapper;

    // return line by activeType
    public CustomPage<LineResponse> getLines(ViewRequestFilter request) {
        PageResult<LineProjection> result = lineQueryPort.findByFilter(
                request.status(), request.toPageable(), request.sortType().toDomain(), request.direction());
        List<LineResponse> content = result.content().stream()
                .map(LineResponse::from)
                .toList();
        return CustomPage.of(content, request.toPageable().getPageNumber(), request.toPageable().getPageSize(), result.totalElements());
    }

    public LineDetailResponse getStationsById(Integer lineId, StatusFilter status) {
        String cachedJson = redisLinePort.getSegments(lineId, status);
        LineProjection line = lineQueryPort.findById(lineId)
                .orElseThrow(()->CustomException.app(AppErrorCode.LINE_NOT_FOUND));

        if (cachedJson != null && !cachedJson.isBlank()) {
            List<LineSegmentRow> cached = tryReadList(cachedJson, lineId, status);
            if (cached!=null) {
                return LineDetailResponse.of(line.id(), line.name(), line.activeType(),
                        cached.stream()
                                .map(StationSegmentResponse::from)
                                .toList());
            }
        }

        List<LineSegmentRow> segments =
                segmentQueryPort.findByLineAndActiveType(lineId, status);

        if (status==StatusFilter.ACTIVE) {
            segments = stationSorter.sortSegments(segments, lineId);// 불변 OK (캐시값은 수정되면 안 됨)
        }

        String json = writeJson(segments);
        redisLinePort.setSegments(lineId, status, json);

        List<StationSegmentResponse> segmentResponses = segments.stream()
                .map(StationSegmentResponse::from)
                .toList();

        return LineDetailResponse.of(line.id(), line.name(), line.activeType(), segmentResponses);
    }

    private List<LineSegmentRow> tryReadList(String json, Integer lineId, StatusFilter status) {
        try {
            return redisObjectMapper.readValue(json, new TypeReference<>() {});
        } catch (Exception e) {
            redisLinePort.evictSegments(lineId);
            // 캐시 깨졌으면 로그 남기고 null 반환
            log.warn("redis cache parse fail. evict. lineId={}, status={}", lineId, status, e);
            return null;
        }
    }

    private String writeJson(List<LineSegmentRow> value) {
        try {
            return redisObjectMapper.writeValueAsString(value);
        } catch (Exception e) {
            throw CustomException.app(AppErrorCode.REDIS_SERIALIZATION_ERROR);
        }
    }
}
