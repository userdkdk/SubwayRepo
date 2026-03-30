package com.example.app.api.station.api.dto.response;

import com.example.app.api.station.port.query.row.StationRow;
import com.example.core.common.domain.enums.ActiveType;

public record StationResponse(
    Integer id,
    String name,
    ActiveType activeType
) {
    public static StationResponse from(StationRow projection) {
        return new StationResponse(
                projection.id(),
                projection.name(),
                projection.activeType()
        );
    }
}
