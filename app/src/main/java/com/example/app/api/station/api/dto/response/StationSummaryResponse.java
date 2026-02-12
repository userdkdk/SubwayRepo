package com.example.app.api.station.api.dto.response;

import com.example.core.common.domain.enums.ActiveType;

import java.util.List;

public record StationSummaryResponse(
    Integer id,
    String name,
    ActiveType activeType,
    List<Integer> lines
) {}
