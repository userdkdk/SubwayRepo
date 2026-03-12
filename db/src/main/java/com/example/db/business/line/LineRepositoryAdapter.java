package com.example.db.business.line;

import com.example.core.common.exception.DomainErrorCode;
import com.example.core.domain.line.Line;
import com.example.core.domain.line.LineName;
import com.example.core.domain.line.LineRepository;
import com.example.core.common.domain.enums.ActiveType;
import com.example.core.common.exception.CustomException;
import com.example.core.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LineRepositoryAdapter implements LineRepository {
    private final SpringDataLineJpaRepository lineJpaRepository;
    private final LineMapper lineMapper;

    @Override
    public Line save(Line line) {
        try {
            LineJpaEntity saved = lineJpaRepository.save(lineMapper.toNewEntity(line));
            lineJpaRepository.flush();
            return lineMapper.toDomain(saved);
        } catch (DataIntegrityViolationException e) {
            throw CustomException.app(DomainErrorCode.LINE_NAME_DUPLICATED)
                    .addParam("name", line.getName());
        }
    }

    @Override
    public Line findByIdForUpdate(Integer id) {
        LineJpaEntity line = lineJpaRepository.findByIdForUpdate(id)
                .orElseThrow(()->CustomException.app(DomainErrorCode.LINE_NOT_FOUND)
                        .addParam("id", id));
        return lineMapper.toDomain(line);
    }

    @Override
    public void updateName(Integer id, LineName name) {
        LineJpaEntity entity = findById(id);
        entity.changeName(name.value());
        tryCommit(DomainErrorCode.LINE_NAME_DUPLICATED, name.value());
    }

    @Override
    public void updateStatus(Integer id, ActiveType activeType) {
        LineJpaEntity entity = findById(id);
        entity.changeActiveType(activeType);
    }

    @Override
    public void activeLine(Integer id) {
        int updated = lineJpaRepository.setActivateById(id, ActiveType.INACTIVE, ActiveType.ACTIVE);
        if (updated != 1) {
            throw CustomException.app(DomainErrorCode.LINE_NOT_FOUND)
                    .addParam("line id", id);
        }
    }

    @Override
    public void deactiveLine(Integer id) {

    }

    @Override
    public boolean existsActiveById(Integer id) {
        return lineJpaRepository.existsByIdAndActiveType(id, ActiveType.ACTIVE);
    }

    private LineJpaEntity findById(Integer id) {
        return lineJpaRepository.findById(id)
                .orElseThrow(()->CustomException.app(DomainErrorCode.LINE_NOT_FOUND)
                        .addParam("id", id));
    }

    private void tryCommit(ErrorCode code, String message) {
        try {
            lineJpaRepository.flush();
        } catch (DataIntegrityViolationException e) {
            throw CustomException.app(code,message);
        }
    }
}
