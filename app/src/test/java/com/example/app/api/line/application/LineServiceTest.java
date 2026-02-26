package com.example.app.api.line.application;

import com.example.app.api.line.api.dto.request.SegmentAttributeRequest;
import com.example.app.api.line.api.dto.request.line.CreateLineRequest;
import com.example.app.api.line.api.dto.request.line.UpdateLineAttributeRequest;
import com.example.db.business.line.LineJpaEntity;
import com.example.app.common.exception.AppErrorCode;
import com.example.app.support.DbHelper;
import com.example.app.support.MySqlFlywayTcConfig;
import com.example.core.exception.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class LineServiceTest extends MySqlFlywayTcConfig {

    @Autowired LineService lineService;
    @Autowired DbHelper dbHelper;

    @BeforeEach
    void clean() {
        dbHelper.truncateAll();
    }

    @ParameterizedTest
    @MethodSource("NotFoundStationId")
    @DisplayName("활성_상태의_start나_end_station_없으면_에러_반환")
    void checkStationExistsForCreateLine(Integer s1, Integer s2) {
        dbHelper.insertStation("station 1");
        dbHelper.insertStation("station 2");
        SegmentAttributeRequest seg = new SegmentAttributeRequest(1.0,2);
        assertThatThrownBy(()->lineService.createLine(new CreateLineRequest("line", s1, s2,seg)))
                .isInstanceOf(CustomException.class)
                .extracting("errorCode")
                .isEqualTo(AppErrorCode.STATION_NOT_FOUND);

    }

    @Test
    @DisplayName("같은_이름_동시_생성시_하나는_저장_하나는_에러_반환")
    void 같은_이름_동시_생성시_하나는_저장_하나는_에러_반환() throws Exception {
        dbHelper.insertStation("station 1");
        dbHelper.insertStation("station 2");

        int threads = 3;
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

                    SegmentAttributeRequest seg = new SegmentAttributeRequest(1.0,2);
                    CreateLineRequest req = new CreateLineRequest("line", 1, 2,seg);
                    lineService.createLine(req);
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

        // 성공 시
        assertEquals(1, dbHelper.countLineByName("line"));

        // 실패는 1개
        assertEquals(2, errors.size());
        CustomException ex = (CustomException) errors.get(0);
        assertEquals(AppErrorCode.LINE_NAME_DUPLICATED, ex.getErrorCode());

        pool.shutdown();
    }

    @Test
    @DisplayName("같은_역_동시_이름변경이면_한쪽은_낙관락_실패")
    void 같은_역_동시_이름변경이면_한쪽은_낙관락_실패() throws Exception {
        LineJpaEntity l = dbHelper.insertLineNoSegment("line 1");
        Integer id = l.getId();

        int threads = 2;
        ExecutorService pool = Executors.newFixedThreadPool(threads);

        CountDownLatch ready = new CountDownLatch(threads);
        CountDownLatch start = new CountDownLatch(1);
        CountDownLatch done = new CountDownLatch(threads);

        List<Throwable> errors = Collections.synchronizedList(new ArrayList<>());

        for (int i = 0; i < threads; i++) {
            final String newName = (i == 0) ? "line A" : "line B";
            pool.submit(() -> {
                try {
                    ready.countDown();
                    start.await();

                    UpdateLineAttributeRequest req = new UpdateLineAttributeRequest(newName);
                    lineService.updateLineAttribute(id,req);
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

        // 하나는 실패해야 한다
        assertEquals(1, errors.size());

        Throwable t = errors.get(0);
        System.out.println("ERROR TYPE "+ t.getClass());
        System.out.println(t.getMessage());
        assertTrue(
                t instanceof org.springframework.orm.ObjectOptimisticLockingFailureException
                        || t instanceof jakarta.persistence.OptimisticLockException
                        || (t.getCause() != null && t.getCause() instanceof jakarta.persistence.OptimisticLockException)
        );

        pool.shutdown();
    }

    @Test
    @DisplayName("동시에_같은_이름으로_수정시_하나는_200_하나는_에러_반환")
    void 동시에_같은_이름으로_수정시_하나는_200_하나는_에러_반환() throws Exception {
        LineJpaEntity l1 = dbHelper.insertLineNoSegment("line 1");
        LineJpaEntity l2 = dbHelper.insertLineNoSegment("line 2");
        Integer[] idArr = {l1.getId(), l2.getId()};

        int threads = 2;
        ExecutorService pool = Executors.newFixedThreadPool(threads);

        CountDownLatch ready = new CountDownLatch(threads);
        CountDownLatch start = new CountDownLatch(1);
        CountDownLatch done = new CountDownLatch(threads);

        List<Throwable> errors = Collections.synchronizedList(new ArrayList<>());

        String newName = "line A";
        for (int i = 0; i < threads; i++) {
            final int idx = i;
            pool.submit(() -> {
                try {
                    ready.countDown();
                    start.await();
                    lineService.updateLineAttribute(idArr[idx],
                            new UpdateLineAttributeRequest(newName));

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

        // 성공 시
        assertEquals(1, dbHelper.countLineByName("line A"));

        // 실패는 1개
        assertEquals(1, errors.size());
        CustomException ex = (CustomException) errors.get(0);
        assertEquals(AppErrorCode.LINE_NAME_DUPLICATED, ex.getErrorCode());

        pool.shutdown();
    }





    static Stream<Arguments> NotFoundStationId() {
        return Stream.of(
                Arguments.of(10, 1),
                Arguments.of(1, 10),
                Arguments.of(0, 1)
        );
    }
}