package com.example.app.api.station.api.dto.request;

import com.example.app.common.response.enums.ActionType;
import jakarta.validation.constraints.NotNull;

public record UpdateStationStatusRequest(
        @NotNull
        ActionType actionType
) {}
