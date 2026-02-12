package com.example.app.business.segment.projection;

import com.example.core.common.domain.enums.ActiveType;
import com.querydsl.core.annotations.QueryProjection;

public record StationSegmentLineIdProjection(
        Integer lineId,
        Integer segmentId,
        ActiveType activeType
) {
    @QueryProjection
    public StationSegmentLineIdProjection {}
}
