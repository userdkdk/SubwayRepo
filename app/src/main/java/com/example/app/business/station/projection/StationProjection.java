package com.example.app.business.station.projection;

import com.example.core.common.domain.enums.ActiveType;
import com.querydsl.core.annotations.QueryProjection;

public record StationProjection(
        Integer id,
        String name,
        ActiveType activeType
) {
    @QueryProjection
    public StationProjection {}
}
