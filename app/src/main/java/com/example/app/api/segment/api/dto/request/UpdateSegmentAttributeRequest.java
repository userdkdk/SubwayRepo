package com.example.app.api.segment.api.dto.request;


import com.example.core.domain.segment.SegmentAttribute;

public record UpdateSegmentAttributeRequest(
        SegmentAttribute attribute
){}
