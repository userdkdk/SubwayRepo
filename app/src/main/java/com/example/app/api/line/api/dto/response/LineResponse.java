package com.example.app.api.line.api.dto.response;

import com.example.app.api.line.port.query.row.LineProjection;
import com.example.core.common.domain.enums.ActiveType;

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
