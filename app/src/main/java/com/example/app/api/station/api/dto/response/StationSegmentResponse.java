package com.example.app.api.station.api.dto.response;

import com.example.core.common.domain.enums.ActiveType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class StationSegmentResponse {
    private Integer beforeId;
    private String beforeName;
    private Integer afterId;
    private String afterName;
    private double distance;
    private int spendTime;
}
