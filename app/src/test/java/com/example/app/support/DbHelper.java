package com.example.app.support;

import com.example.app.business.line.LineJpaEntity;
import com.example.app.business.line.SpringDataLineJpaRepository;
import com.example.app.business.segment.SegmentJpaEntity;
import com.example.app.business.segment.SpringDataSegmentJpaRepository;
import com.example.app.business.station.SpringDataStationJpaRepository;
import com.example.app.business.station.StationJpaEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DbHelper {

    private final JdbcTemplate jdbcTemplate;
    private final SpringDataStationJpaRepository stationRepository;
    private final SpringDataLineJpaRepository lineRepository;
    private final SpringDataSegmentJpaRepository segmentRepository;

    public StationJpaEntity insertStation(String name) {
        return stationRepository.save(TestFixture.station(name));
    }

    public LineJpaEntity insertLine(String name) {
        return lineRepository.save(TestFixture.line(name));
    }

    public SegmentJpaEntity insertSegment(LineJpaEntity line,
                                          StationJpaEntity s1,
                                          StationJpaEntity s2,
                                          Double distance,
                                          Integer spendTime) {
        return segmentRepository.save(TestFixture.segment(
                line, s1, s2, distance, spendTime
        ));
    }

    public StationJpaEntity getStationById(Integer id) {
        return stationRepository.findById(id).orElseThrow();
    }

    public int countByName(String name) {
        return stationRepository.countByName(name);
    }

    public void truncateAll() {
        jdbcTemplate.execute("SET FOREIGN_KEY_CHECKS = 0");
        jdbcTemplate.execute("TRUNCATE TABLE `segment_histories`");
        jdbcTemplate.execute("TRUNCATE TABLE `segments`");
        jdbcTemplate.execute("TRUNCATE TABLE `line_stations`");
        jdbcTemplate.execute("TRUNCATE TABLE `lines`");
        jdbcTemplate.execute("TRUNCATE TABLE `stations`");
        jdbcTemplate.execute("SET FOREIGN_KEY_CHECKS = 1");

    }
}
