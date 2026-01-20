package com.example.app.business.segment;

import com.example.core.common.domain.enums.ActiveType;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SpringDataSegmentJpaRepository extends JpaRepository<SegmentJpaEntity, Integer> {

    boolean existsByLineJpaEntity_IdAndBeforeStationJpaEntity_IdAndAfterStationJpaEntity_Id(
            Integer lineId, Integer beforeStationId, Integer afterStationId
    );

    boolean existsByBeforeStationJpaEntity_IdAndAfterStationJpaEntity_IdAndActiveType(Integer stationId, Integer stationId1, ActiveType activeType);

    @EntityGraph(attributePaths = {
            "beforeStationJpaEntity",
            "afterStationJpaEntity"
    })
    List<SegmentJpaEntity> findByLineJpaEntity_IdAndActiveType(Integer lineId, ActiveType activeType);
}
