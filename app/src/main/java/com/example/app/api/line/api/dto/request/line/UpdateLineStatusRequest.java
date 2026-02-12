package com.example.app.api.line.api.dto.request.line;

import com.example.app.common.dto.request.enums.ActionType;

public record UpdateLineStatusRequest(
        ActionType actionType
) {}
