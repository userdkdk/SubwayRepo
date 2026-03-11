package com.example.core.domain.path.port.data;

public record PathSegmentData(
        Integer beforeStationId,
        Integer afterStationId,
        Double distance,
        Integer spendTime
){}
