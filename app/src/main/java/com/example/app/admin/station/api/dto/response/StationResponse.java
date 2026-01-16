package com.example.app.admin.station.api.dto.response;

import com.example.core.common.domain.enums.ActiveType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class StationResponse {
    String name;
    ActiveType activeType;
}
