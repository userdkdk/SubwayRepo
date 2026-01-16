package com.example.app.business.line.adapter;

import com.example.core.business.line.Line;
import org.springframework.stereotype.Component;

@Component
public class LineMapper {
    public LineJpaEntity toEntity(Line line) {
        return LineJpaEntity.create(
                line.getName(),
                line.getActiveType(),
                line.getCounts());
    }
}
