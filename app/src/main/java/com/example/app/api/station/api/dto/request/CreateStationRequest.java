package com.example.app.api.station.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class CreateStationRequest {
    @NotBlank
    private String name;
}
