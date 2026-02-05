package com.example.app.business.line;

import com.example.app.common.exception.AppErrorCode;
import com.example.core.business.line.Line;
import com.example.core.business.line.LineRepository;
import com.example.core.common.domain.enums.ActiveType;
import com.example.core.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@Component
@RequiredArgsConstructor
public class LineRepositoryAdapter implements LineRepository {
    private final SpringDataLineJpaRepository lineJpaRepository;
    private final LineMapper lineMapper;

    @Override
    public Line save(Line line) {
        String name = line.getName();

        if (lineJpaRepository.existsByName(name)) {
            throw CustomException.app(AppErrorCode.LINE_NAME_DUPLICATED)
                    .addParam("name", name);
        }

        LineJpaEntity saved = lineJpaRepository.save(
                lineMapper.toNewEntity(line)
        );
        return lineMapper.toDomain(saved);
    }

    @Override
    public void update(Integer id, Consumer<Line> updater) {
        LineJpaEntity entity = lineJpaRepository.findById(id)
                .orElseThrow(()->CustomException.app(AppErrorCode.LINE_NOT_FOUND)
                        .addParam("id", id));
        Line line = lineMapper.toDomain(entity);

        updater.accept(line);
        String newName = line.getName();
        if (!newName.equals(entity.getName())
                && lineJpaRepository.existsByName(newName)) {
            throw CustomException.app(AppErrorCode.LINE_NAME_DUPLICATED);
        }

        entity.setName(line.getName());
        entity.setActiveType(line.getActiveType());
    }

    @Override
    public boolean existsActiveById(Integer id) {
        return lineJpaRepository.existsByIdAndActiveType(id, ActiveType.ACTIVE);
    }
}
