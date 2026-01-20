package com.example.app.business.segment;

import com.example.core.business.station.Station;
import com.example.core.common.domain.enums.ActiveType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SegmentQueryRepository {
    private final SpringDataSegmentJpaRepository segmentJpaRepository;

    public boolean existsByStationId(Station station) {
        Integer stationId = station.getId();
        return segmentJpaRepository.existsByBeforeStationJpaEntity_IdAndAfterStationJpaEntity_IdAndActiveType(
                stationId, stationId, ActiveType.ACTIVE
        );
    }

    public List<SegmentJpaEntity> findByLine(Integer lineId) {
        return segmentJpaRepository.findByLineJpaEntity_IdAndActiveType(
                lineId, ActiveType.ACTIVE
        );
    }
}
