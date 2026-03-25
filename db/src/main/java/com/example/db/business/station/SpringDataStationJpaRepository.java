package com.example.db.business.station;

import com.example.core.common.domain.enums.ActiveType;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SpringDataStationJpaRepository extends JpaRepository<StationJpaEntity, Integer> {

    boolean existsByName(String name);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select s from StationJpaEntity s " +
            "where s.id = :id")
    Optional<StationJpaEntity> findByIdForUpdate(@Param("id") Integer id);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select s from StationJpaEntity s " +
            "where s.id = :id and s.activeType = :activeType")
    Optional<StationJpaEntity> findByIdAndActivateForUpdate(
            @Param("id") Integer id,
            @Param("activeType") ActiveType activeType);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select s from StationJpaEntity s " +
            "where s.id in (:ids) and s.activeType = :activeType")
    List<StationJpaEntity> findAllByIdsAndActiveTypeForUpdate(
            @Param("ids") List<Integer> ids,
            @Param("activeType") ActiveType activeType);

    boolean existsByIdAndActiveType(Integer id, ActiveType activeType);

    // test
    int countByName(String name);
}
