package com.example.app.api.station.api.dto.response;

import com.example.core.common.domain.enums.ActiveType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class StationResponse {
    private final String name;
    private final ActiveType activeType;
}
