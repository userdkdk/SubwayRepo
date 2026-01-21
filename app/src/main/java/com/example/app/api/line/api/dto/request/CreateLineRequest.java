package com.example.app.api.line.api.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class CreateLineRequest {
    @NotNull
    private String name;
    @NotNull
    private Integer startId;
    @NotNull
    private Integer endId;
    @NotNull
    private double distance;
    @NotNull
    private int spendTime;
}
