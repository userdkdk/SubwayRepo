package com.example.app.business.line;

import com.example.app.common.exception.AppErrorCode;
import com.example.core.business.line.Line;
import com.example.core.business.line.LineName;
import com.example.core.business.line.LineRepository;
import com.example.core.common.domain.enums.ActiveType;
import com.example.core.exception.CustomException;
import com.example.core.exception.ErrorCode;
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
            throw CustomException.app(AppErrorCode.LINE_NAME_DUPLICATED)
                    .addParam("name", line.getName());
        }
    }

    @Override
    public void updateAttribute(Integer id, LineName name) {
        LineJpaEntity entity = findById(id);
        entity.changeName(name.value());
        tryCommit(AppErrorCode.LINE_NAME_DUPLICATED, "중복된 이름입니다.");
    }

    @Override
    public void deActiveLine(Integer id) {

    }

    @Override
    public void activeLine(Integer id) {

    }

    @Override
    public boolean existsActiveById(Integer id) {
        return lineJpaRepository.existsByIdAndActiveType(id, ActiveType.ACTIVE);
    }

    private LineJpaEntity findById(Integer id) {
        return lineJpaRepository.findById(id)
                .orElseThrow(()->CustomException.app(AppErrorCode.LINE_NOT_FOUND)
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
