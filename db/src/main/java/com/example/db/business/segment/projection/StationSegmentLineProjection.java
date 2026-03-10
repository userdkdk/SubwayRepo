package com.example.db.business.segment.projection;

import com.example.core.common.domain.enums.ActiveType;
import com.querydsl.core.annotations.QueryProjection;

public record StationSegmentLineProjection(
        Integer lineId,
        Integer segmentId,
        ActiveType activeType,
        Integer beforeStationId,
        String beforeStationName,
        Integer afterStationId,
        String afterStationName
) {
    @QueryProjection
    public StationSegmentLineProjection {}
}
