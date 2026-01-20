package com.example.app.business.station;

import com.example.core.common.domain.enums.ActiveType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SpringDataStationJpaRepository extends JpaRepository<StationJpaEntity, Integer> {

    Optional<StationJpaEntity> findByName(String name);

    boolean existsByName(String name);

    @Query("select s.id from StationJpaEntity s where s.name = :name")
    Optional<Integer> findIdByName(@Param("name") String name);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("update StationJpaEntity s " +
            "set s.activeType = :active " +
            "where s.name = :name")
    int setActivateByName(@Param("name") String name, @Param("active") ActiveType activeType);

    List<StationJpaEntity> findAllByActiveType(ActiveType activeType);
}
