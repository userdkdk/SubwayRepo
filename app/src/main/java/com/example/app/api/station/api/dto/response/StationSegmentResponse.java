package com.example.app.api.station.api.dto.response;

import com.example.app.api.line.port.cache.LineDetailSegmentCacheItem;
import com.example.core.common.domain.enums.ActiveType;

public record StationSegmentResponse(
        Integer segmentId,
        Integer beforeId,
        String beforeName,
        Integer afterId,
        String afterName,
        ActiveType status,
        Double distance,
        Integer spendTime
) {
    public static StationSegmentResponse from(LineDetailSegmentCacheItem cache) {
        return new StationSegmentResponse(
                cache.segmentId(),
                cache.beforeId(),
                cache.beforeName(),
                cache.afterId(),
                cache.afterName(),
                cache.activeType(),
                cache.distance(),
                cache.spendTime()
        );
    }
}
