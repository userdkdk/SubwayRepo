package com.example.app.business.segment;

import com.example.core.common.domain.enums.ActiveType;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SpringDataSegmentJpaRepository extends JpaRepository<SegmentJpaEntity, Integer> {

    @EntityGraph(attributePaths = {
            "beforeStationJpaEntity",
            "afterStationJpaEntity"
    })
    List<SegmentJpaEntity> findByLineJpaEntity_IdAndActiveType(Integer lineId, ActiveType activeType);

    Optional<SegmentJpaEntity> findByLineJpaEntity_idAndBeforeStationJpaEntity_idAndActiveType(Integer lineId, Integer stationId, ActiveType activeType);

    Optional<SegmentJpaEntity> findByLineJpaEntity_idAndAfterStationJpaEntity_idAndActiveType(Integer lineId, Integer stationId, ActiveType activeType);

    Optional<SegmentJpaEntity> findByLineJpaEntity_idAndBeforeStationJpaEntity_idAndAfterStationJpaEntity_idAndActiveType(Integer lineId, Integer startId, Integer endId, ActiveType activeType);
}
