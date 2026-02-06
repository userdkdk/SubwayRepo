package com.example.app.api.line.api.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CreateSegmentRequest (
        @NotNull @Positive
        Integer stationId,

        @Positive
        Integer beforeId,

        @Positive
        Integer afterId,

        @Positive
        Double beforeDistance,

        @Positive
        Integer beforeSpendTime,

        @Positive
        Double afterDistance,

        @Positive
        Integer afterSpendTime
) {}
