package com.example.app.api.station.port.query.row;

import com.example.core.common.domain.enums.ActiveType;

public record StationSegmentRow(
        Integer lineId,
        Integer segmentId,
        ActiveType activeType,
        Integer beforeStationId,
        String beforeStationName,
        Integer afterStationId,
        String afterStationName
) {}
