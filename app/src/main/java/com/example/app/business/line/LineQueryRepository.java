package com.example.app.business.line;

import com.example.app.business.segment.SegmentJpaEntity;
import com.example.app.common.exception.AppErrorCode;
import com.example.app.common.response.enums.StatusFilter;
import com.example.core.common.domain.enums.ActiveType;
import com.example.core.common.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LineQueryRepository {
    private final SpringDataLineJpaRepository lineJpaRepository;
    private final LineMapper lineMapper;

    public boolean existsActiveById(Integer id) {
        return lineJpaRepository.existsByIdAndActiveType(id, ActiveType.ACTIVE);
    }

    public List<LineJpaEntity> findByActiveType(StatusFilter status) {
        if (status != StatusFilter.ALL) {
            return lineJpaRepository.findByActiveType(status.toActiveType());
        }
        return lineJpaRepository.findAll();
    }
}
