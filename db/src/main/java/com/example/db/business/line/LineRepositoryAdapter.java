package com.example.db.business.line;

import com.example.core.common.exception.DomainErrorCode;
import com.example.db.common.exception.DbErrorCode;
import com.example.core.business.line.Line;
import com.example.core.business.line.LineName;
import com.example.core.business.line.LineRepository;
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
    public void ensureExistsForUpdate(Integer id) {
        lineJpaRepository.findByIdForUpdate(id)
                .orElseThrow(()->CustomException.app(DomainErrorCode.LINE_NOT_FOUND)
                        .addParam("id", id));
    }

    @Override
    public void updateAttribute(Integer id, LineName name) {
        LineJpaEntity entity = lineJpaRepository.findById(id)
                .orElseThrow(()->CustomException.app(DomainErrorCode.LINE_NOT_FOUND)
                        .addParam("id", id));
        entity.changeName(name.value());
        tryCommit(DomainErrorCode.LINE_NAME_DUPLICATED, "중복된 이름입니다.");
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

    private void tryCommit(ErrorCode code, String message) {
        try {
            lineJpaRepository.flush();
        } catch (DataIntegrityViolationException e) {
            throw CustomException.app(code,message);
        }
    }
}
