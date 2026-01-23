package com.example.app.api.station.api.dto.request;

import com.example.app.common.response.enums.StatusFilter;
import lombok.Getter;

@Getter
public class UpdateStationRequest {
    private String name;
    private StatusFilter status;
}
