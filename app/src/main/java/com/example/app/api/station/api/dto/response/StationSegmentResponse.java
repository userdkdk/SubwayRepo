package com.example.app.api.station.api.dto.response;

import com.example.app.api.line.port.row.LineSegmentRow;

public record StationSegmentResponse(
        Integer segmentId,
        Integer beforeId,
        String beforeName,
        Integer afterId,
        String afterName,
        Double distance,
        Integer spendTime
) {
    public static StationSegmentResponse from(LineSegmentRow row) {
        return new StationSegmentResponse(
                row.segmentId(),
                row.beforeStationId(),
                row.beforeStationName(),
                row.afterStationId(),
                row.afterStationName(),
                row.distance(),
                row.spendTime()
        );
    }
}
