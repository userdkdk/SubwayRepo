package com.example.app.api.line.port.query.row;

import com.example.core.common.domain.enums.ActiveType;

public record LineSegmentRow(
        Integer segmentId,
        Integer beforeStationId,
        String beforeStationName,
        Integer afterStationId,
        String afterStationName,
        ActiveType activeType,
        Double distance,
        Integer spendTime
) {}
