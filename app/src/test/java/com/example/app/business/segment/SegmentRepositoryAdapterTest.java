package com.example.app.business.segment;

import com.example.app.business.line.LineJpaEntity;
import com.example.app.business.station.StationJpaEntity;
import com.example.app.support.DbHelper;
import com.example.app.support.MySqlFlywayTcConfig;
import com.example.core.business.segment.Segment;
import com.example.core.business.segment.SegmentAttribute;
import com.example.core.common.domain.enums.ActiveType;
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
@Import({SegmentRepositoryAdapter.class, DbHelper.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class SegmentRepositoryAdapterTest extends MySqlFlywayTcConfig {

    @Autowired SegmentRepositoryAdapter segmentRepo;
    @Autowired TestEntityManager em;
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
}