package com.example.app.business.line.adapter;

import com.example.core.business.line.Line;
import com.example.core.business.line.LineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LineRepositoryAdapter implements LineRepository {
    private final SpringDataLineJpaRepository lineJpaRepository;
    private final LineMapper lineMapper;

    @Override
    public boolean existsByName(String name) {
        return lineJpaRepository.existsByName(name);
    }

    @Override
    public void save(Line line) {
        lineJpaRepository.save(lineMapper.toEntity(line));
    }

    @Override
    public Line findByName(String name) {
        return null;
    }

    @Override
    public void active(Line line) {
        LineJpaEntity entity = lineMapper.toEntity(line);
        entity.active();
    }
}
