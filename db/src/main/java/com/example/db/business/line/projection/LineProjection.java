package com.example.db.business.line.projection;

import com.example.core.common.domain.enums.ActiveType;
import com.querydsl.core.annotations.QueryProjection;

public record LineProjection(
        Integer id,
        String name,
        ActiveType activeType
) {
    @QueryProjection
    public LineProjection {}
}
