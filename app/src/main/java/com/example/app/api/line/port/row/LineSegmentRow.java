package com.example.app.api.line.port.row;

public record LineSegmentRow(
        Integer segmentId,
        Integer beforeStationId,
        String beforeStationName,
        Integer afterStationId,
        String afterStationName,
        Double distance,
        Integer spendTime
) {}
