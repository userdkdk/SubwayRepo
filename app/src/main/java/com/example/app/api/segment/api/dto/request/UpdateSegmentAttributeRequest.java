package com.example.app.api.segment.api.dto.request;


import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record UpdateSegmentAttributeRequest(
        @NotNull @Valid SegmentAttributeRequest attribute
){}
