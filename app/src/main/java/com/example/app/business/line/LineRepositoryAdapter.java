package com.example.app.business.line;

import com.example.core.business.line.Line;
import com.example.core.business.line.LineRepository;
import com.example.core.common.domain.enums.ActiveType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LineRepositoryAdapter implements LineRepository {
    private final SpringDataLineJpaRepository lineJpaRepository;
    private final LineMapper lineMapper;

    @Override
    public Line upsertActivateByName(String rawName, Integer start, Integer end) {
        Line line = Line.create(rawName, start, end);
        String name = line.getName();

        int isUpdated = lineJpaRepository.setActivateByName(name, ActiveType.ACTIVE);
        Integer lineId;
        if (isUpdated==0) {
            LineJpaEntity saved = lineJpaRepository.save(
                    LineJpaEntity.create(name, ActiveType.ACTIVE,2)
            );
            lineId = saved.getId();
        } else {

        }
        return null;
    }
}
