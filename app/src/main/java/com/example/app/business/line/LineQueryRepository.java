package com.example.app.business.line;

import com.example.core.business.line.Line;
import com.example.core.common.domain.enums.ActiveType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LineQueryRepository {
    private final SpringDataLineJpaRepository lineJpaRepository;
    private final LineMapper lineMapper;

    public boolean existsById(Integer id) {
        return lineJpaRepository.existsByIdAndActiveType(id, ActiveType.ACTIVE);
    }

}
