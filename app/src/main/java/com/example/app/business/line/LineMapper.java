package com.example.app.business.line;

import com.example.core.business.line.Line;
import org.springframework.stereotype.Component;

@Component
public class LineMapper {
    public LineJpaEntity toNewEntity(Line line) {
        return LineJpaEntity.create(
                line.getName());
    }

    public Line toDomain(LineJpaEntity saved) {
        return Line.of(saved.getId(),
                saved.getName(),
                saved.getCounts(),
                saved.getActiveType());
    }
}
