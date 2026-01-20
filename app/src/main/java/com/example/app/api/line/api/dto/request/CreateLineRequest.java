package com.example.app.api.line.api.dto.request;

import lombok.Getter;

@Getter
public class CreateLineRequest {
    private String name;
    private Integer startId;
    private Integer endId;
    private double distance;
    private int spendTime;
}
