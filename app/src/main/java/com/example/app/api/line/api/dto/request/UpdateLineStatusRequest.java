package com.example.app.api.line.api.dto.request;

import com.example.app.common.response.enums.ActionType;

public record UpdateLineStatusRequest(
        ActionType actionType
) {}
