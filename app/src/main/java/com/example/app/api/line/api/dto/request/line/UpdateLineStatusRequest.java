package com.example.app.api.line.api.dto.request.line;

import com.example.app.api.line.api.dto.request.SegmentAttributeRequest;
import com.example.app.common.dto.request.enums.ActionType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record UpdateLineStatusRequest(
        @NotNull
        ActionType actionType
) {}
