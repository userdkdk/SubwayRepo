package com.example.app.api.line.port.cache;

import com.example.app.api.line.port.query.row.LineSegmentRow;
import com.example.core.common.domain.enums.ActiveType;

public record LineDetailSegmentCacheItem(
        Integer segmentId,
        Integer beforeId,
        String beforeName,
        Integer afterId,
        String afterName,
        ActiveType activeType,
        Double distance,
        Integer spendTime
) {
    public static LineDetailSegmentCacheItem from(LineSegmentRow row) {
        return new LineDetailSegmentCacheItem(
                row.segmentId(),
                row.beforeStationId(),
                row.beforeStationName(),
                row.afterStationId(),
                row.afterStationName(),
                row.activeType(),
                row.distance(),
                row.spendTime()
        );
    }
}
