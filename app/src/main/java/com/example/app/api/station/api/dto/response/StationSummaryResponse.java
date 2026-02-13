package com.example.app.api.station.api.dto.response;

import com.example.app.business.station.projection.StationProjection;
import com.example.core.common.domain.enums.ActiveType;

public record StationSummaryResponse(
    Integer id,
    String name,
    ActiveType activeType
) {
    public static StationSummaryResponse from(StationProjection projection) {
        return new StationSummaryResponse(
                projection.id(),
                projection.name(),
                projection.activeType()
        );
    }
}
