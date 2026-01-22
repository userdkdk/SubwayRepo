package com.example.app.business.lineStation;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SpringDataLineStationJpaRepository extends JpaRepository<LineStationJpaEntity, Integer> {
    Optional<LineStationJpaEntity> findByLineJpaEntity_IdAndStationJpaEntity_Id(int i, int i1);

}
