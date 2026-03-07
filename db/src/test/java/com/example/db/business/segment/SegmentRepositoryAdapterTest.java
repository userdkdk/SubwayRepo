package com.example.db.business.segment;

import com.example.core.common.domain.enums.ActiveType;
import com.example.core.domain.segment.Segment;
import com.example.core.domain.segment.SegmentAttribute;
import com.example.core.domain.station.StationConnectionInfo;
import com.example.core.domain.station.StationRoleInLine;
import com.example.db.business.line.LineJpaEntity;
import com.example.db.business.station.StationJpaEntity;
import com.example.db.support.DbHelper;
import com.example.db.support.MySqlFlywayTcConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import({SegmentRepositoryAdapter.class, DbHelper.class, SegmentMapper.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class SegmentRepositoryAdapterTest extends MySqlFlywayTcConfig {
    @Autowired
    SegmentRepositoryAdapter segmentRepo;
    @Autowired
    TestEntityManager em;
    @Autowired DbHelper dbHelper;

    @BeforeEach
    void clean() {
        dbHelper.truncateAll();
    }

    @Test
    @DisplayName("저장시_역_노선_조합이_없으면_새로_생성한다")
    void 저장시_역_노선_조합이_없으면_새로_생성한다() throws Exception {
        dbHelper.insertStation("station 1");
        dbHelper.insertStation("station 2");
        dbHelper.insertLineNoSegment("line 1");

        SegmentAttribute seg = new SegmentAttribute(1.0,2);
        Segment segment = Segment.create(1,1,2,seg);
        segmentRepo.upsert(segment);
        SegmentJpaEntity reloaded = dbHelper.getSegmentById(1);
        assertTrue(reloaded.getBeforeStationJpaEntity().getId()==1 &&
                reloaded.getAfterStationJpaEntity().getId()==2 &&
                reloaded.getLineJpaEntity().getId()==1 &&
                reloaded.getActiveType()== ActiveType.ACTIVE);
    }

    @Test
    @DisplayName("저장시_역_노선_조합이_이미_존재하는_경우_활성화한다")
    void 저장시_역_노선_조합이_이미_존재하는_경우_활성화한다() throws Exception {
        StationJpaEntity s1 = dbHelper.insertStation("station 1");
        StationJpaEntity s2 = dbHelper.insertStation("station 2");
        LineJpaEntity l = dbHelper.insertLineNoSegment("line 1");
        dbHelper.insertSegment(l,s1,s2,1.0,2,ActiveType.INACTIVE);

        SegmentAttribute seg = new SegmentAttribute(2.57,4);
        Segment segment = Segment.create(l.getId(), s1.getId(), s2.getId(), seg);
        segmentRepo.upsert(segment);
        Integer id = segmentRepo.findIdByUniqueKey(segment);

        SegmentJpaEntity reloaded = dbHelper.getSegmentById(id);
        assertEquals(s1.getId(), reloaded.getBeforeStationJpaEntity().getId());
        assertEquals(s2.getId(), reloaded.getAfterStationJpaEntity().getId());
        assertEquals(reloaded.getDistance(), seg.distance());
        assertEquals(reloaded.getActiveType(), ActiveType.ACTIVE);
    }

    @Test
    void existsActiveStation() {
        StationJpaEntity s1 = dbHelper.insertStation("station 1");
        StationJpaEntity s2 = dbHelper.insertStation("station 2");
        LineJpaEntity line = dbHelper.insertLine("line 1", s1, s2, 3.3, 4);

        em.flush();
        em.clear();

        assertTrue(segmentRepo.existsActiveSegmentByStation(s1.getId()));
    }

    @Test
    @DisplayName("주변 역 정보 가져올때 역의 위치를 얻을 수 있다")
    void FindStationConnectionLocationTest() {
        StationJpaEntity s1 = dbHelper.insertStation("station 1");
        StationJpaEntity s2 = dbHelper.insertStation("station 2");
        StationJpaEntity s3 = dbHelper.insertStation("station 3");
        StationJpaEntity s4 = dbHelper.insertStation("station 4");
        LineJpaEntity l = dbHelper.insertLine("line", s1, s2, 1.2, 3);
        dbHelper.insertSegment(l,s2,s3,1.0,3,ActiveType.ACTIVE);

        StationConnectionInfo head = segmentRepo.findRemovableInfo(l.getId(), s1.getId());
        assertEquals(head.role(),StationRoleInLine.HEAD);

        StationConnectionInfo internal = segmentRepo.findRemovableInfo(l.getId(), s2.getId());
        assertEquals(internal.role(),StationRoleInLine.INTERNAL);

        StationConnectionInfo tail = segmentRepo.findRemovableInfo(l.getId(), s3.getId());
        assertEquals(tail.role(),StationRoleInLine.TAIL);

        StationConnectionInfo not = segmentRepo.findRemovableInfo(l.getId(), s4.getId());
        assertEquals(not.role(),StationRoleInLine.NOT_IN_LINE);
    }
}