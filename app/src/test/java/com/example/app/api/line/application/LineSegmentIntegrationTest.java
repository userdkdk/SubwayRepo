package com.example.app.api.line.application;

import com.example.app.api.line.api.dto.request.SegmentAttributeRequest;
import com.example.app.api.line.api.dto.request.segment.CreateSegmentRequest;
import com.example.app.support.IntegrationTest;
import com.example.core.domain.station.StationRoleInLine;
import com.example.db.business.line.LineJpaEntity;
import com.example.db.business.segment.SegmentRepositoryAdapter;
import com.example.db.business.station.StationJpaEntity;
import com.example.db.support.DbHelper;
import com.example.db.support.MySqlFlywayTcConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;

public class LineSegmentIntegrationTest extends IntegrationTest {

    @Autowired
    LineSegmentService lineSegmentService;
    @Autowired
    SegmentRepositoryAdapter segmentRepositoryAdapter;
    @Autowired
    DbHelper dbHelper;

    @BeforeEach
    void clean() {
        dbHelper.truncateAll();
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("position")
    @DisplayName("station 추가 정상 수행 검증")
    void addStationTest(String displayName, Integer position) {
        StationJpaEntity s1 = dbHelper.insertStation("station 1");
        StationJpaEntity s2 = dbHelper.insertStation("station 2");
        StationJpaEntity s3 = dbHelper.insertStation("station 3");
        LineJpaEntity l = dbHelper.insertLine("line", s1, s2, 1.2, 3);

        SegmentAttributeRequest seg = new SegmentAttributeRequest(1.2, 3);
        // internal
        CreateSegmentRequest req = switch (position) {
            case 0 -> new CreateSegmentRequest(null, s1.getId(), seg, seg);
            case 1 -> new CreateSegmentRequest(s1.getId(), s2.getId(), seg, seg);
            case 2 -> new CreateSegmentRequest(s2.getId(), null, seg, seg);
            default -> throw new IllegalStateException("Unexpected value: " + position);
        };
        lineSegmentService.addStation(l.getId(), s3.getId(), req);
        assertEquals(2,dbHelper.countActiveSegmentByLineId(l.getId()));

        StationRoleInLine role = segmentRepositoryAdapter.findActiveRole(l.getId(), s3.getId());
        switch (position) {
            case 0 -> assertEquals(StationRoleInLine.HEAD,role);
            case 1 -> assertEquals(StationRoleInLine.INTERNAL,role);
            case 2 -> assertEquals(StationRoleInLine.TAIL,role);
        }
    }

    static Stream<Arguments> position() {
        return Stream.of(
                Arguments.of("head", 0),
                Arguments.of("internal", 1),
                Arguments.of("tail", 2)
        );
    }
}
