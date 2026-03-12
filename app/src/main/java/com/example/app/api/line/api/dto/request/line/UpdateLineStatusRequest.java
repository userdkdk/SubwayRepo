package com.example.app.api.line.api.dto.request.line;

import com.example.app.common.dto.request.enums.ActionType;
import jakarta.validation.constraints.NotNull;

public record UpdateLineStatusRequest(
        @NotNull
        ActionType actionType
) {}
