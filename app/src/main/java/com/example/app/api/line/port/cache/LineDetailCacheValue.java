package com.example.app.api.line.port.cache;

import com.example.app.api.line.api.dto.response.LineDetailResponse;
import com.example.app.api.line.port.query.row.LineProjection;
import com.example.app.api.line.port.query.row.LineSegmentRow;
import com.example.app.api.station.api.dto.response.StationSegmentResponse;
import com.example.core.common.domain.enums.ActiveType;

import java.util.List;

public record LineDetailCacheValue(
        Integer id,
        String name,
        ActiveType activeType,
        List<LineDetailSegmentCacheItem> segments
) {
    public LineDetailCacheValue {
        segments = List.copyOf(segments);
    }
    public static LineDetailCacheValue of(
            LineProjection line,
            List<LineSegmentRow> rows
    ) {
        return new LineDetailCacheValue(
                line.id(),
                line.name(),
                line.activeType(),
                rows.stream()
                        .map(LineDetailSegmentCacheItem::from)
                        .toList()
        );
    }

    public LineDetailResponse toResponse() {
        return LineDetailResponse.of(
                id,
                name,
                activeType,
                segments.stream()
                        .map(StationSegmentResponse::from)
                        .toList()
        );
    }
}
