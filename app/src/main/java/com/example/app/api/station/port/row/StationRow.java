package com.example.app.api.station.port.row;

import com.example.core.common.domain.enums.ActiveType;

public record StationRow(
        Integer id,
        String name,
        ActiveType activeType
) {}
