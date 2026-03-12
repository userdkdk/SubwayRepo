package com.example.app.api.segment.api.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record SegmentAttributeRequest(
        @NotNull @Min(0) @Max(1000) Double distance,
        @NotNull @Min(0) @Max(1000) Integer spendTime
) {}
