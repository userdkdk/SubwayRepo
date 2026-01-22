package com.example.app.api.station.api.dto.response;

import com.example.core.common.domain.enums.ActiveType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class StationSegmentResponse {
    private final Integer segmentId;
    private final Integer beforeId;
    private final String beforeName;
    private final Integer afterId;
    private final String afterName;
    private final Double distance;
    private final Integer spendTime;
}
