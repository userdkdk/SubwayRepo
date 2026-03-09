package com.example.app.api.station.application;

import com.example.app.api.station.api.dto.request.CreateStationRequest;
import com.example.app.api.station.api.dto.request.UpdateStationAttributeRequest;
import com.example.app.api.station.api.dto.request.UpdateStationStatusRequest;
import com.example.app.support.IntegrationTest;
import com.example.core.common.exception.DomainErrorCode;
import com.example.db.business.station.StationJpaEntity;
import com.example.app.common.dto.request.enums.ActionType;
import com.example.app.common.exception.AppErrorCode;
import com.example.db.support.ConcurrentRunner;
import com.example.db.support.DbHelper;
import com.example.core.common.domain.enums.ActiveType;
import com.example.core.common.exception.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class StationServiceTest extends IntegrationTest {

    @Autowired StationService stationService;
    @Autowired DbHelper dbHelper;

    @BeforeEach
    void clean() {
        dbHelper.truncateAll();
    }

    @Test
    @DisplayName("같은_이름_동시_생성시_하나는_저장_하나는_에러_반환")
    void 같은_이름_동시_생성시_하나는_저장_하나는_에러_반환() throws Exception {
        int threads = 2;
        ConcurrentRunner.Result result = ConcurrentRunner.run(threads, (i)-> {
            stationService.createStation(new CreateStationRequest("station A"));
        });

        // 성공 시
        assertEquals(1, dbHelper.countStationByName("station A"));

        // 실패는 1개
        assertEquals(1, result.errorCount());
        List<CustomException> domainErrors = result.errorsOf(CustomException.class);
        domainErrors.forEach(ex ->
                assertEquals(DomainErrorCode.STATION_NAME_DUPLICATED, ex.getErrorCode())
        );
    }

    @Test
    @DisplayName("같은_station_동시_이름변경이면_한쪽은_낙관락_실패")
    void 같은_station_동시_이름변경이면_한쪽은_낙관락_실패() throws Exception {
        StationJpaEntity s = dbHelper.insertStation("station 1");
        Integer id = s.getId();

        int threads = 2;
        ConcurrentRunner.Result result = ConcurrentRunner.run(threads, (i)-> {
            final String newName = (i == 0) ? "station A" : "station B";
            stationService.updateStationAttribute(id,
                    new UpdateStationAttributeRequest(newName));
        });

        // 하나는 실패
        assertEquals(1, result.errorCount());
        List<Exception> domainErrors = result.errorsOf(Exception.class);
    }

    @Test
    @DisplayName("동시에_같은_이름으로_수정시_하나는_200_하나는_에러_반환")
    void 동시에_같은_이름으로_수정시_하나는_200_하나는_에러_반환() throws Exception {
        StationJpaEntity s1 = dbHelper.insertStation("station 1");
        StationJpaEntity s2 = dbHelper.insertStation("station 2");
        Integer[] idArr = {s1.getId(), s2.getId()};
        String newName = "station A";

        int threads = 2;
        ConcurrentRunner.Result result = ConcurrentRunner.run(threads, (i)-> {
            stationService.updateStationAttribute(idArr[i],
                    new UpdateStationAttributeRequest(newName));
        });

        // 성공 시
        assertEquals(1, dbHelper.countStationByName("station A"));

        // 실패는 1개
        assertEquals(1, result.errorCount());
        List<CustomException> domainErrors = result.errorsOf(CustomException.class);
        domainErrors.forEach(ex ->
                assertEquals(DomainErrorCode.STATION_NAME_DUPLICATED, ex.getErrorCode())
        );
    }

    @Test
    @DisplayName("같은_상태로_변경은_정상_동작")
    void 같은_상태로_변경은_정상_동작() {
        StationJpaEntity s = dbHelper.insertStation("station 1");
        UpdateStationStatusRequest req = new UpdateStationStatusRequest(ActionType.ACTIVE);
        stationService.updateStationStatus(s.getId(), req);

        StationJpaEntity reloaded = dbHelper.getStationById(1);
        assertEquals(ActiveType.ACTIVE, reloaded.getActiveType());
    }

    @Test
    @DisplayName("비활성화시_활동중인_seg가_있으면_에러")
    void 비활성화시_활동중인_seg가_있으면_에러() {
        StationJpaEntity s1 = dbHelper.insertStation("station 1");
        StationJpaEntity s2 = dbHelper.insertStation("station 2");
        dbHelper.insertLine("line 1", s1, s2,1.0,4);

        UpdateStationStatusRequest req = new UpdateStationStatusRequest(ActionType.INACTIVE);

        assertThatThrownBy(()-> stationService.updateStationStatus(s1.getId(), req))
                .isInstanceOf(CustomException.class)
                .extracting("errorCode")
                .isEqualTo(AppErrorCode.ACTIVE_STATION_EXISTS);
    }

    @Test
    @DisplayName("해당_엔티티가_존재하지_않으면_에러_반환")
    void 해당_엔티티가_존재하지_않으면_에러_반환() {
        UpdateStationStatusRequest req = new UpdateStationStatusRequest(ActionType.ACTIVE);

        assertThatThrownBy(()-> stationService.updateStationStatus(1, req))
                .isInstanceOf(CustomException.class)
                .extracting("errorCode")
                .isEqualTo(DomainErrorCode.STATION_NOT_FOUND);
    }
}