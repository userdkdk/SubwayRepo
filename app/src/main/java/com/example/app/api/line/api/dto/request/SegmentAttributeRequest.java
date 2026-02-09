package com.example.app.api.line.api.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record SegmentAttributeRequest(
        @NotNull @Positive Double distance,
        @NotNull @Positive Integer spendTime
) {}
