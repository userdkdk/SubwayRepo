package com.example.app.business.lineStation.adapter;

import com.example.core.business.lineStation.LineStation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SpringDataLineStationJpaRepository extends JpaRepository<LineStationJpaEntity, Integer> {
    Optional<LineStation> findByLineJpaEntityIdAndStationJpaEntityId(int i, int i1);
}
