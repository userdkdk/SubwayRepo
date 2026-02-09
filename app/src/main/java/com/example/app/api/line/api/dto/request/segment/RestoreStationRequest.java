package com.example.app.api.line.api.dto.request.segment;

import com.example.app.api.line.api.dto.request.SegmentAttributeRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record RestoreStationRequest(
        @NotNull Integer beforeId,
        @NotNull Integer afterId,
        @Valid @NotNull SegmentAttributeRequest before,
        @Valid @NotNull SegmentAttributeRequest after
) {
}
