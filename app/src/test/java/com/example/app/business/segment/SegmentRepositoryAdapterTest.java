package com.example.app.business.segment;

import com.example.app.api.line.api.dto.request.SegmentAttributeRequest;
import com.example.app.api.line.api.dto.request.line.CreateLineRequest;
import com.example.app.business.line.LineJpaEntity;
import com.example.app.business.station.StationJpaEntity;
import com.example.app.common.exception.AppErrorCode;
import com.example.app.support.DbHelper;
import com.example.app.support.MySqlFlywayTcConfig;
import com.example.core.business.segment.Segment;
import com.example.core.business.segment.SegmentAttribute;
import com.example.core.common.domain.enums.ActiveType;
import com.example.core.exception.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import({SegmentRepositoryAdapter.class, SegmentMapper.class, DbHelper.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class SegmentRepositoryAdapterTest extends MySqlFlywayTcConfig {

    @Autowired SegmentRepositoryAdapter segmentRepo;
    @Autowired TestEntityManager em;
    @Autowired DbHelper dbHelper;
    @Autowired TransactionTemplate txTemplate;

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
        segmentRepo.save(segment);
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
        Integer id = segmentRepo.save(segment);

        em.flush();
        em.clear();
        SegmentJpaEntity reloaded = dbHelper.getSegmentById(id);
        assertEquals(s1.getId(), reloaded.getBeforeStationJpaEntity().getId());
        assertEquals(s2.getId(), reloaded.getAfterStationJpaEntity().getId());
        assertEquals(reloaded.getDistance(), seg.distance());
        assertEquals(reloaded.getActiveType(), ActiveType.ACTIVE);
    }


    @Test
    @DisplayName("동시_같은_역_노선으로_저장시_하나는_정상_수행_하나는_에러_반환")
    void 동시_같은_역_노선으로_저장시_하나는_정상_수행_하나는_에러_반환() throws Exception {
        StationJpaEntity s1 = dbHelper.insertStation("station 1");
        StationJpaEntity s2 = dbHelper.insertStation("station 2");
        LineJpaEntity l = dbHelper.insertLineNoSegment("line 1");

        int threads = 2;
        ExecutorService pool = Executors.newFixedThreadPool(threads);

        CountDownLatch ready = new CountDownLatch(threads);
        CountDownLatch start = new CountDownLatch(1);
        CountDownLatch done = new CountDownLatch(threads);

        List<Throwable> errors = Collections.synchronizedList(new ArrayList<>());

        for (int i = 0; i < threads; i++) {
            pool.submit(() -> {
                try {
                    ready.countDown();
                    start.await();

                    txTemplate.executeWithoutResult(status-> {
                        SegmentAttribute seg = new SegmentAttribute(1.0,2);
                        Segment segment = Segment.create(l.getId(),s1.getId(),s2.getId(),seg);
                        segmentRepo.save(segment);
                    });
                } catch (Throwable t) {
                    errors.add(t);
                } finally {
                    done.countDown();
                }
            });
        }

        ready.await();
        start.countDown();
        done.await();

        errors.forEach(e -> {
            System.out.println("ERROR TYPE = " + e.getClass());
            e.printStackTrace();
        });


        // 성공 시
        assertEquals(1, dbHelper.countSegment());

        // 실패는 1개
        assertEquals(1, errors.size());
        CustomException ex = (CustomException) errors.get(0);
        assertEquals(AppErrorCode.SEGMENT_ALREADY_EXISTS, ex.getErrorCode());

        pool.shutdown();
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