package com.example.app.business.line;

import com.example.core.business.line.Line;
import com.example.core.business.line.LineRepository;
import com.example.core.common.domain.enums.ActiveType;
import com.example.core.common.exception.CustomException;
import com.example.core.common.exception.DomainErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LineRepositoryAdapter implements LineRepository {
    private final SpringDataLineJpaRepository lineJpaRepository;
    private final LineMapper lineMapper;

    @Override
    public Line save(Line line) {
        String name = line.getName();

        if (lineJpaRepository.existsByName(name)) {
            throw CustomException.app(DomainErrorCode.LINE_NAME_DUPLICATED)
                    .addParam("name", name);
        }

        LineJpaEntity saved = lineJpaRepository.save(
                lineMapper.toNewEntity(line)
        );
        return lineMapper.toDomain(saved);
    }

    @Override
    public void inActivate(Line line) {

    }

    @Override
    public void Activate(Line line) {

    }

    public Line upsertActivateByName(String rawName, Integer start, Integer end) {
        Line line = Line.create(rawName);
        String name = line.getName();

        int isUpdated = lineJpaRepository.setActivateByName(name, ActiveType.ACTIVE);
        Integer lineId;
        return null;
    }
}
