package com.example.app.api.line.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;

@Getter
public class CreateLineRequest {
    @NotBlank
    private String name;
    @NotNull @Positive
    private Integer startId;
    @NotNull @Positive
    private Integer endId;
    @NotNull @Positive
    private Double distance;
    @NotNull @Positive
    private Integer spendTime;
}
