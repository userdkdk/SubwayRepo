package com.example.db.business.segment;

import com.example.core.domain.station.StationConnectionInfo;
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

    Optional<SegmentJpaEntity> findByLineJpaEntity_IdAndBeforeStationJpaEntity_IdAndAfterStationJpaEntity_Id(
            Integer lineId, Integer beforeId, Integer afterId
    );

    @Query("""
        select count(s) > 0
        from SegmentJpaEntity s
        where s.activeType = :activeType
            and (s.beforeStationJpaEntity.id = :stationId or s.afterStationJpaEntity.id = :stationId)
    """)
    boolean existsActiveSegmentByStation(
            @Param("stationId") Integer stationId,
            @Param("activeType") ActiveType activeType);

    @Query("""
        select count(s) > 0
        from SegmentJpaEntity s
        where s.lineJpaEntity.id = :lineId
            and s.activeType = :activeType
            and (s.beforeStationJpaEntity.id = :stationId or s.afterStationJpaEntity.id = :stationId)
    """)
    boolean existsStationByLineAndActiveType(
            @Param("lineId") Integer lineId,
            @Param("stationId") Integer stationId,
            @Param("activeType") ActiveType activeType);

    @Query("""
        select new com.example.db.business.segment.projection.RoleCount(
          coalesce(sum(case when s.beforeStationJpaEntity.id = :stationId then 1 else 0 end), 0),
          coalesce(sum(case when s.afterStationJpaEntity.id  = :stationId then 1 else 0 end), 0)
        )
        from SegmentJpaEntity s
        where s.lineJpaEntity.id = :lineId
          and s.activeType = :active
    """)
    RoleCount countRole(
            @Param("lineId") Integer lineId,
            @Param("stationId") Integer stationId,
            @Param("active") ActiveType active);

    @Query("""
            select new com.example.core.domain.station.StationConnectionInfo(
                max(case when s.afterStationJpaEntity.id = :stationId then s.beforeStationJpaEntity.id else null end),
                max(case when s.beforeStationJpaEntity.id = :stationId then s.afterStationJpaEntity.id else null end),
                max(case when s.afterStationJpaEntity.id = :stationId then s.distance else null end),
                max(case when s.afterStationJpaEntity.id = :stationId then s.spendTime else null end),
                max(case when s.beforeStationJpaEntity.id = :stationId then s.distance else null end),
                max(case when s.beforeStationJpaEntity.id = :stationId then s.spendTime else null end)
            )
            from SegmentJpaEntity s
            where s.activeType = com.example.core.common.domain.enums.ActiveType.ACTIVE
                and s.lineJpaEntity.id = :lineId
                and (s.beforeStationJpaEntity.id = :stationId
                    or s.afterStationJpaEntity.id = :stationId)
            """)
    Optional<StationConnectionInfo> findStationConnection(
            @Param("lineId") Integer lineId,
            @Param("stationId") Integer stationId);

    @Modifying
    @Query("""
           update SegmentJpaEntity s
           set s.activeType = :inactive
           where s.lineJpaEntity.id = :lineId
           and s.beforeStationJpaEntity.id = :beforeId
           and s.afterStationJpaEntity.id = :afterId
           and s.activeType = :active
    """)
    int inactivateActiveSegment(
            @Param("lineId") Integer lineId,
            @Param("beforeId") Integer beforeId,
            @Param("afterId") Integer afterId,
            @Param("inactive") ActiveType inactive,
            @Param("active") ActiveType active);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query(value = """
            update SegmentJpaEntity s
            set s.activeType = :target
            where s.id in (:segIds)
                and s.activeType = :from
            """)
    int changeActivateByIdAndLineId(@Param("segIds") List<Integer> segIds,
                                    @Param("target") ActiveType target,
                                    @Param("from") ActiveType from);

    int countByLineJpaEntity_IdAndActiveType(Integer lineId, ActiveType activeType);

    @Query("""
        select s.id
        from SegmentJpaEntity s
        where s.lineJpaEntity.id = :lineId
          and s.activeType = :activeType
    """)
    List<Integer> findIdsByLineIdAndActiveType(
            @Param("lineId") Integer lineId,
            @Param("activeType") ActiveType activeType);

    @Query(value = """
            select distinct station_id
            from (
                select s.before_station_id as station_id
                from segments s
                where s.id in (:segmentIds)
               \s
                union
               \s
                select s.after_station_id as station_id
                from segments s
                where s.id in (:segmentIds)
            ) t
           order by station_id
           """, nativeQuery = true)
    List<Integer> findStationIdsBySegmentsIds(
            @Param("segmentIds") List<Integer> segmentIds);

    // test
    @EntityGraph(attributePaths = {
            "beforeStationJpaEntity",
            "afterStationJpaEntity"
    })
    List<SegmentJpaEntity> findByLineJpaEntity_IdAndActiveType(Integer lineId, ActiveType activeType);
}
