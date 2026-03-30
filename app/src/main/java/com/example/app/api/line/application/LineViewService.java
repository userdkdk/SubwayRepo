package com.example.app.api.line.application;

import com.example.app.api.line.api.dto.response.LineDetailResponse;
import com.example.app.api.line.api.dto.response.LineResponse;
import com.example.app.api.line.port.cache.LineDetailCacheValue;
import com.example.app.api.line.port.query.LineQueryPort;
import com.example.app.api.line.port.query.row.LineProjection;
import com.example.app.api.line.port.query.row.LineSegmentRow;
import com.example.app.api.segment.port.query.SegmentQueryPort;
import com.example.app.common.dto.page.PageResult;
import com.example.app.common.dto.request.ViewRequestFilter;
import com.example.app.common.dto.page.CustomPage;
import com.example.app.common.dto.request.enums.StatusFilter;
import com.example.app.common.exception.AppErrorCode;
import com.example.app.api.line.port.cache.LineDetailCachePort;
import com.example.core.common.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LineViewService {

    private final LineQueryPort lineQueryPort;
    private final SegmentQueryPort segmentQueryPort;
    private final StationSorter stationSorter;
    private final LineDetailCachePort lineDetailCachePort;

    // return line by activeType
    public CustomPage<LineResponse> getLines(ViewRequestFilter request) {
        PageResult<LineProjection> result = lineQueryPort.findByFilter(
                request.status(), request.toPageable(), request.sortType().toDomain(), request.direction());
        List<LineResponse> content = result.content().stream()
                .map(LineResponse::from)
                .toList();
        return CustomPage.of(content, request.toPageable().getPageNumber(), request.toPageable().getPageSize(), result.totalElements());
    }

    public LineDetailResponse getLineDetailById(Integer lineId, StatusFilter status) {
        Optional<LineDetailCacheValue> cached = lineDetailCachePort.get(lineId, status);
        log.info("isCached "+ cached.isPresent());
        if (cached.isPresent()) {
            return cached.get().toResponse();
        }
        log.info("not cached");
        LineProjection line = lineQueryPort.findById(lineId)
                .orElseThrow(() -> CustomException.app(AppErrorCode.LINE_NOT_FOUND));

        List<LineSegmentRow> segments =
                segmentQueryPort.findByLineAndActiveType(lineId, status);

        if (status == StatusFilter.ACTIVE) {
            segments = stationSorter.sortSegments(segments, lineId);
        }

        LineDetailCacheValue cacheValue = LineDetailCacheValue.of(line, segments);
        lineDetailCachePort.set(lineId, status, cacheValue);

        return cacheValue.toResponse();
    }

    public LineDetailResponse getLineDetailByName(String name, StatusFilter status) {

        return null;
    }
}
