package com.example.app.api.station.api.dto.request;

import jakarta.validation.constraints.NotBlank;

public record UpdateStationAttributeRequest (
        @NotBlank
        String name
) {}
