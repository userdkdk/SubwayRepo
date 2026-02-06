package com.example.app.business.line;

import com.example.core.business.line.Line;
import com.example.core.business.line.LineName;
import org.springframework.stereotype.Component;

@Component
public class LineMapper {
    public LineJpaEntity toNewEntity(Line line) {
        return LineJpaEntity.create(
                line.getName().value(),
                line.getActiveType());
    }

    public Line toDomain(LineJpaEntity saved) {
        return Line.of(saved.getId(),
                new LineName(saved.getName()),
                saved.getActiveType());
    }
}
