package com.example.app.api.segment.api.dto.request;


import com.example.app.common.response.enums.StatusFilter;
import com.example.core.business.segment.SegmentAttribute;

public record UpdateSegmentRequest (
        StatusFilter status,
        SegmentAttribute attribute
){}
