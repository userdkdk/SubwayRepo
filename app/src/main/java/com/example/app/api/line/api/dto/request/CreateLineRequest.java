package com.example.app.api.line.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CreateLineRequest (
        @NotBlank
        String name,

        @NotNull @Positive
        Integer startId,

        @NotNull @Positive
        Integer endId,

        @NotNull @Positive
        Double distance,

        @NotNull @Positive
        Integer spendTime
) {}
