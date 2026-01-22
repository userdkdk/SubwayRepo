package com.example.app.api.line.api.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;

@Getter
public class CreateSegmentRequest {
    @NotNull @Positive
    private Integer stationId;
    @Positive
    private Integer beforeId;
    @Positive
    private Integer afterId;
    @Positive
    private Double beforeDistance;
    @Positive
    private Integer beforeSpendTime;
    @Positive
    private Double afterDistance;
    @Positive
    private Integer afterSpendTime;
}
