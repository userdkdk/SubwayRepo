package com.example.app.business.segment;

import com.example.app.common.response.enums.StatusFilter;
import com.example.core.common.domain.enums.ActiveType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class SegmentQueryRepository {
    private final SpringDataSegmentJpaRepository segmentJpaRepository;

    public List<SegmentJpaEntity> findByLineAndActiveType(Integer lineId, StatusFilter status) {
        if (status != StatusFilter.ALL) {
            return segmentJpaRepository.findByLineJpaEntity_IdAndActiveType(
                    lineId, status.toActiveType()
            );
        }
        return segmentJpaRepository.findByLineJpaEntity_Id(lineId);
    }

    public List<SegmentJpaEntity> findAllActive() {
        return segmentJpaRepository.findByActiveType(ActiveType.ACTIVE);
    }

}
