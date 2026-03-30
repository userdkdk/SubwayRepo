package com.example.app.api.line.port.query.row;

import com.example.core.common.domain.enums.ActiveType;

public record LineProjection(
        Integer id,
        String name,
        ActiveType activeType
) {}
