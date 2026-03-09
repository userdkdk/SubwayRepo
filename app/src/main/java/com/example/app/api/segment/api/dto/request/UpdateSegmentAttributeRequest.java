package com.example.app.api.segment.api.dto.request;


import com.example.app.api.line.api.dto.request.SegmentAttributeRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record UpdateSegmentAttributeRequest(
        @NotNull @Valid SegmentAttributeRequest attribute
){}
