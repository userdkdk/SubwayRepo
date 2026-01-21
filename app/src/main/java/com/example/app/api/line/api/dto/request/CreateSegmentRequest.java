package com.example.app.api.line.api.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class CreateSegmentRequest {
    @NotNull
    private Integer stationId;
    private Integer beforeId;
    private Integer afterId;
    private double beforeDistance;
    private int beforeSpendTime;
    private double afterDistance;
    private int afterSpendTime;
}
