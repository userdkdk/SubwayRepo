package com.example.app.api.line.api.dto.request.line;

import com.example.app.api.line.api.dto.request.SegmentAttributeRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CreateLineRequest (
        @NotBlank String name,
        @NotNull @Positive Integer startId,
        @NotNull @Positive Integer endId,
        @Valid @NotNull SegmentAttributeRequest attribute
) {}
