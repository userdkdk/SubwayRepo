package com.example.app.api.line.api.dto.request;

import lombok.Getter;

@Getter
public class CreateSegmentRequest {
    private Integer startId;
    private Integer endId;
    private double distance;
    private int spendTime;
}
