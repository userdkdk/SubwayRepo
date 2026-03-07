package com.example.app.api.station.api.dto.response;

import com.example.db.business.station.projection.StationProjection;
import com.example.core.common.domain.enums.ActiveType;

public record StationResponse(
    Integer id,
    String name,
    ActiveType activeType
) {
    public static StationResponse from(StationProjection projection) {
        return new StationResponse(
                projection.id(),
                projection.name(),
                projection.activeType()
        );
    }
}
