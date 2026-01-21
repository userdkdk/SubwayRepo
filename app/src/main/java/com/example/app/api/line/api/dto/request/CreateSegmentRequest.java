package com.example.app.api.line.api.dto.request;

import lombok.Getter;

@Getter
public class CreateSegmentRequest {
    private Integer stationId;
    private Integer beforeId;
    private Integer afterId;
    private double beforeDistance;
    private double afterDistance;
    private int beforeSpendTime;
    private int afterSpendTime;
}
