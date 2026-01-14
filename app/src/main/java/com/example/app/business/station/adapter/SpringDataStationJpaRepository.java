package com.example.app.business.station.adapter;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataStationJpaRepository extends JpaRepository<StationJpaEntity, Integer> {
    boolean existsByName(String name);
}
