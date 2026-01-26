package com.example.app.api.station.api.dto.response;

public record StationSegmentResponse(
        Integer segmentId,
        Integer beforeId,
        String beforeName,
        Integer afterId,
        String afterName,
        Double distance,
        Integer spendTime
) {}
