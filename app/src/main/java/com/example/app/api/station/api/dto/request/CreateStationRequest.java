package com.example.app.api.station.api.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class CreateStationRequest {
    @NotNull
    private String name;
}
