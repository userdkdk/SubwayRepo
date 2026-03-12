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
    @Query("select s from StationJpaEntity s where s.id = :id")
    Optional<StationJpaEntity> findByIdForUpdate(@Param("id") Integer id);

//    List<StationJpaEntity> findByActiveType(ActiveType activeType);

    boolean existsByIdAndActiveType(Integer id, ActiveType activeType);

    // test
    int countByName(String name);
}
