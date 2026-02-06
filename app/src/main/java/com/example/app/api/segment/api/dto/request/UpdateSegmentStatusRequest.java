package com.example.app.api.segment.api.dto.request;


import com.example.app.common.response.enums.ActionType;

public record UpdateSegmentStatusRequest(
        ActionType actionType
){}
