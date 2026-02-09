package com.example.app.api.line.api.dto.request.segment;

import com.example.app.api.line.api.dto.request.SegmentAttributeRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;

public record RestoreStationRequest(
        @Positive Integer beforeId,
        @Positive Integer afterId,
        @Valid SegmentAttributeRequest before,
        @Valid SegmentAttributeRequest after
) {
}
