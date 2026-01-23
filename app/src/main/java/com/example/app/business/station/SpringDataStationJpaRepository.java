package com.example.app.business.station;

import com.example.core.common.domain.enums.ActiveType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SpringDataStationJpaRepository extends JpaRepository<StationJpaEntity, Integer> {

    boolean existsByName(String name);
    List<StationJpaEntity> findByActiveType(ActiveType activeType);
    boolean existsByIdAndActiveType(Integer id, ActiveType activeType);
    Optional<StationJpaEntity> findByIdAndActiveType(Integer stationId, ActiveType activeType);
}
