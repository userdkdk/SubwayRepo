package com.example.app.api.station.application;

import com.example.app.api.station.api.dto.request.CreateStationRequest;
import com.example.app.business.station.StationJpaEntity;
import com.example.app.common.exception.AppErrorCode;
import com.example.app.support.DbHelper;
import com.example.app.support.MySqlFlywayTcConfig;
import com.example.core.exception.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class StationServiceTest extends MySqlFlywayTcConfig {

    @Autowired StationService stationService;
    @Autowired DbHelper dbHelper;

    @BeforeEach
    void clean() {
        dbHelper.truncateAll();
    }

    @Test
    @DisplayName("정상_생성되면_DB에_저장된다")
    void 정상_생성되면_DB에_저장된다() {
        stationService.createStation(new CreateStationRequest(" station 1 "));

        StationJpaEntity reloaded = dbHelper.getStationById(1);
        assertEquals("station 1", reloaded.getName());
    }

    @Test
    @DisplayName("이름_중복이면_로직에서_에러_빈환")
    void 이름_중복이면_로직에서_에러_빈환() {
        dbHelper.insertStation("station 1");

        assertThatThrownBy(()->stationService.createStation(new CreateStationRequest("station 1")))
                .isInstanceOf(CustomException.class)
                .extracting("errorCode")
                .isEqualTo(AppErrorCode.STATION_NAME_DUPLICATED);
    }

    @Test
    @DisplayName("같은_이름_동시_생성시_하나는_저장_하나는_에러_반환")
    void 같은_이름_동시_생성시_하나는_저장_하나는_에러_반환() throws Exception {
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
                    stationService.createStation(new CreateStationRequest("station A"));
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
        assertEquals(1, dbHelper.countByName("station A"));

        // 실패는 1개
        assertEquals(1, errors.size());
        CustomException ex = (CustomException) errors.get(0);
        assertEquals(AppErrorCode.STATION_NAME_DUPLICATED, ex.getErrorCode());
    }
}