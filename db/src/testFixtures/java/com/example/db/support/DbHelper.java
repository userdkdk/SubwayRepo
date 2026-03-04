package com.example.db.support;

import com.example.core.common.domain.enums.ActiveType;
import com.example.db.business.line.LineJpaEntity;
import com.example.db.business.line.SpringDataLineJpaRepository;
import com.example.db.business.lineSnapshot.LineSnapshotJpaEntity;
import com.example.db.business.lineSnapshot.LineSnapshotSegmentJpaEntity;
import com.example.db.business.lineSnapshot.SpringDataLineSnapshotJpaRepository;
import com.example.db.business.lineSnapshot.SpringDataLineSnapshotSegmentJpaRepository;
import com.example.db.business.segment.SegmentJpaEntity;
import com.example.db.business.segment.SpringDataSegmentJpaRepository;
import com.example.db.business.station.SpringDataStationJpaRepository;
import com.example.db.business.station.StationJpaEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DbHelper {

    @PersistenceContext
    private final EntityManager em;
    private final JdbcTemplate jdbcTemplate;
    private final SpringDataStationJpaRepository stationRepository;
    private final SpringDataLineJpaRepository lineRepository;
    private final SpringDataSegmentJpaRepository segmentRepository;
    private final SpringDataLineSnapshotJpaRepository snapshotRepository;
    private final SpringDataLineSnapshotSegmentJpaRepository snapshotSegmentJpaRepository;

    public StationJpaEntity insertStation(String name) {
        return stationRepository.save(TestFixture.station(name));
    }

    public LineJpaEntity insertLine(String name,
                                    StationJpaEntity s1,
                                    StationJpaEntity s2,
                                    Double distance,
                                    Integer spendTime) {
        LineJpaEntity line = lineRepository.save(TestFixture.line(name));
        segmentRepository.save(TestFixture.segment(
                line, s1, s2, distance, spendTime, ActiveType.ACTIVE
        ));
        return line;
    }

    public LineJpaEntity insertLineNoSegment(String name) {
        return lineRepository.save(TestFixture.line(name));
    }

    public SegmentJpaEntity insertSegment(LineJpaEntity line,
                                          StationJpaEntity s1,
                                          StationJpaEntity s2,
                                          Double distance,
                                          Integer spendTime,
                                          ActiveType activeType) {
        return segmentRepository.save(TestFixture.segment(
                line, s1, s2, distance, spendTime, activeType
        ));
    }

    public LineSnapshotJpaEntity insertSnapshot(Integer lineId) {
        return snapshotRepository.save(TestFixture.snapshot(lineId));
    }

    public LineSnapshotSegmentJpaEntity insertSnapshotSegment(Integer snapshotId, Integer segmentId) {
        return snapshotSegmentJpaRepository.save(TestFixture.snapshotSegment(snapshotId,segmentId));
    }

    public StationJpaEntity getStationById(Integer id) {
        return stationRepository.findById(id).orElseThrow();
    }

    public LineJpaEntity getLineById(Integer id) { return lineRepository.findById(id).orElseThrow(); }

    public SegmentJpaEntity getSegmentById(Integer id) {
        return segmentRepository.findById(id).orElseThrow();
    }

    public int countStationByName(String name) {
        return stationRepository.countByName(name);
    }

    public int countLineByName(String name) {
        return lineRepository.countByName(name);
    }

    public long countSegment() {
        return segmentRepository.count();
    }

    public int countSnapshotSegmentsBySnapshotId(Integer snapshotId) {
        return snapshotSegmentJpaRepository.countByIdSnapshotId(snapshotId);
    }

    public int countActiveSegmentByLineId(Integer lineId) {
        return segmentRepository.findByLineJpaEntity_Id(lineId).size();
    }

    public void truncateAll() {
        jdbcTemplate.execute("SET FOREIGN_KEY_CHECKS = 0");
        jdbcTemplate.execute("TRUNCATE TABLE `segment_histories`");
        jdbcTemplate.execute("TRUNCATE TABLE `segments`");
        jdbcTemplate.execute("TRUNCATE TABLE `lines`");
        jdbcTemplate.execute("TRUNCATE TABLE `stations`");
        jdbcTemplate.execute("TRUNCATE TABLE `line_snapshots`");
        jdbcTemplate.execute("TRUNCATE TABLE `line_snapshots_segments`");
        jdbcTemplate.execute("SET FOREIGN_KEY_CHECKS = 1");

    }
}
