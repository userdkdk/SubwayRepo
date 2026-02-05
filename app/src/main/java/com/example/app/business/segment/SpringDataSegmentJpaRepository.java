package com.example.app.business.segment;

import com.example.core.common.domain.enums.ActiveType;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SpringDataSegmentJpaRepository extends JpaRepository<SegmentJpaEntity, Integer> {

    List<SegmentJpaEntity> findByLineJpaEntity_Id(Integer lineId);

    List<SegmentJpaEntity> findByActiveType(ActiveType activeType);
    @EntityGraph(attributePaths = {
            "beforeStationJpaEntity",
            "afterStationJpaEntity"
    })
    List<SegmentJpaEntity> findByLineJpaEntity_IdAndActiveType(Integer lineId, ActiveType activeType);

    // check exists station
    @Query("""
        select count(s) > 0
        from SegmentJpaEntity s
        where s.activeType = :activeType
            and (s.beforeStationJpaEntity.id = :stationId or s.afterStationJpaEntity.id = :stationId)
    """)
    boolean existsActiveStation(Integer stationId, ActiveType activeType);

    // check exists station in line
    @Query("""
        select count(s) > 0
        from SegmentJpaEntity s
        where s.lineJpaEntity.id = :lineId
            and s.activeType = :activeType
            and (s.beforeStationJpaEntity.id = :stationId or s.afterStationJpaEntity.id = :stationId)
    """)
    boolean existsActiveStationInLine(Integer lineId, Integer stationId, ActiveType activeType);
    boolean existsByLineJpaEntity_IdAndBeforeStationJpaEntity_IdAndActiveType(Integer lineId, Integer stationId, ActiveType activeType);
    boolean existsByLineJpaEntity_IdAndAfterStationJpaEntity_IdAndActiveType(Integer lineId, Integer stationId, ActiveType activeType);

    @Modifying
    @Query("""
            update SegmentJpaEntity s
           set s.activeType = :inactive
         where s.lineJpaEntity.id = :lineId
           and s.beforeStationJpaEntity.id = :beforeId
           and s.afterStationJpaEntity.id = :afterId
           and s.activeType = :active
    """)
    int inactivateActivateSegment(Integer lineId, Integer beforeId, Integer afterId, ActiveType inactive, ActiveType active);
}
