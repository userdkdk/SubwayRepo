package com.example.app.business.segment;

import com.example.app.business.line.LineJpaEntity;
import com.example.app.business.line.SpringDataLineJpaRepository;
import com.example.app.business.station.SpringDataStationJpaRepository;
import com.example.app.business.station.StationJpaEntity;
import com.example.app.support.DbCleaner;
import com.example.app.support.MySqlFlywayTcConfig;
import com.example.core.common.domain.enums.ActiveType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import({SegmentRepositoryAdapter.class, SegmentMapper.class,DbCleaner.class})
class SegmentRepositoryAdapterTest extends MySqlFlywayTcConfig {

    @Autowired SegmentRepositoryAdapter adapter;
    @Autowired SpringDataSegmentJpaRepository segmentRepository;
    @Autowired SpringDataStationJpaRepository stationRepository;
    @Autowired SpringDataLineJpaRepository lineRepository;
    @Autowired TestEntityManager em;
    @Autowired DbCleaner dbCleaner;

    @BeforeEach
    void clean() {
        dbCleaner.truncateAll();
    }

    @Test
    void existsActiveStationInLine() {
    }

    @Test
    void existsActiveStation() {
        StationJpaEntity s1 = stationRepository.save(
                StationJpaEntity.create(
                        "station 1", ActiveType.ACTIVE)
        );
        StationJpaEntity s2 = stationRepository.save(
                StationJpaEntity.create(
                        "station 2", ActiveType.ACTIVE)
        );
        LineJpaEntity line = lineRepository.save(
                LineJpaEntity.create("line 1", ActiveType.ACTIVE)
        );
        SegmentJpaEntity seg = segmentRepository.save(
                SegmentJpaEntity.create(line, s1, s2, 3.3, 4)
        );

        segmentRepository.save(seg);
        em.flush();
        em.clear();

        assertTrue(adapter.existsActiveStation(1));
    }

    @Test
    void findActiveRole() {
    }

    @Test
    void inactivateActiveSegment() {
    }
}