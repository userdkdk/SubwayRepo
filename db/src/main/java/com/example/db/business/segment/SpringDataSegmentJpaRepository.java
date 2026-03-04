package com.example.db.business.segment;

import com.example.db.business.segment.projection.RoleCount;
import com.example.core.common.domain.enums.ActiveType;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SpringDataSegmentJpaRepository extends JpaRepository<SegmentJpaEntity, Integer> {

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query(value= """
            insert into `segments`
                (line_id, before_station_id, after_station_id,
                status, distance, spend_time, created_at, updated_at)
            values
                (:lineId, :beforeId, :afterId,
                :status, :distance, :spendTime, NOW(), NOW())
            on duplicate key update
                status = values(status),
                distance = values(distance),
                spend_time = values(spend_time),
                updated_at = now()
            """, nativeQuery = true)
    int upsertSegment(
            @Param("lineId") Integer lineId,
            @Param("beforeId") Integer beforeId,
            @Param("afterId") Integer afterId,
            @Param("status") String status,
            @Param("distance") Double distance,
            @Param("spendTime") Integer spendTime
    );

    List<SegmentJpaEntity> findByLineJpaEntity_Id(Integer lineId);

    List<SegmentJpaEntity> findByActiveType(ActiveType activeType);

    Optional<SegmentJpaEntity> findByLineJpaEntity_IdAndBeforeStationJpaEntity_IdAndAfterStationJpaEntity_Id(
            Integer lineId, Integer beforeId, Integer afterId
    );

    @EntityGraph(attributePaths = {
            "beforeStationJpaEntity",
            "afterStationJpaEntity"
    })
    List<SegmentJpaEntity> findByLineJpaEntity_IdAndActiveType(Integer lineId, ActiveType activeType);

    // check exists segment by station
    @Query("""
        select count(s) > 0
        from SegmentJpaEntity s
        where s.activeType = :activeType
            and (s.beforeStationJpaEntity.id = :stationId or s.afterStationJpaEntity.id = :stationId)
    """)
    boolean existsActiveSegmentByStation(
            @Param("stationId") Integer stationId,
            @Param("activeType") ActiveType activeType);

    // check exists station in line
    @Query("""
        select count(s) > 0
        from SegmentJpaEntity s
        where s.lineJpaEntity.id = :lineId
            and s.activeType = :activeType
            and (s.beforeStationJpaEntity.id = :stationId or s.afterStationJpaEntity.id = :stationId)
    """)
    boolean existsStationByLineAndActiveType(Integer lineId, Integer stationId, ActiveType activeType);

    @Query("""
        select new com.example.db.business.segment.projection.RoleCount(
          coalesce(sum(case when s.beforeStationJpaEntity.id = :stationId then 1 else 0 end), 0),
          coalesce(sum(case when s.afterStationJpaEntity.id  = :stationId then 1 else 0 end), 0)
        )
        from SegmentJpaEntity s
        where s.lineJpaEntity.id = :lineId
          and s.activeType = :active
    """)
    RoleCount countRole(Integer lineId, Integer stationId, ActiveType active);

    // check exists segment by line
    @Query("""
        select count(s) > 0
        from SegmentJpaEntity s
        where s.activeType = :activeType
            and s.lineJpaEntity.id = :lineId
    """)
    boolean existsActiveSegmentByLine(Integer lineId, ActiveType activeType);

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

    @Modifying
    @Query("""
            update SegmentJpaEntity s
            set s.activeType = :activeType
            where s.lineJpaEntity.id = :id
            """)
    int inactivateByLine(Integer id, ActiveType activeType);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query(value = """
            update segments s
            join line_snapshots_segments lss
                on lss.segment_id = s.id
                and lss.snapshot_id = :snapshotId
            set s.status = 'INACTIVE'
            where s.status = 'ACTIVE'
            """, nativeQuery = true)
    int deactivateAllBySnapshotId(@Param("snapshotId") Integer snapshotId);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query(value = """
            update segments
            set status = 'ACTIVE'
            where id in (:segIds)
                and status = 'INACTIVE'
            """, nativeQuery = true)
    int activateByIdAndLineId(@Param("segIds")List<Integer> segIds);
}
