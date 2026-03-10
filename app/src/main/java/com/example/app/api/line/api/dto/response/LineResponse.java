package com.example.app.api.line.api.dto.response;

import com.example.core.common.domain.enums.ActiveType;
import com.example.db.business.line.projection.LineProjection;

public record LineResponse (
        Integer id,
        String name,
        ActiveType activeType
){
    public static LineResponse from(LineProjection projection) {
        return new LineResponse(
                projection.id(),
                projection.name(),
                projection.activeType()
        );
    }
}
