package com.example.app.business.line;

import com.example.app.common.response.enums.StatusFilter;
import com.example.core.common.domain.enums.ActiveType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LineQueryRepository {
    private final SpringDataLineJpaRepository lineJpaRepository;

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
