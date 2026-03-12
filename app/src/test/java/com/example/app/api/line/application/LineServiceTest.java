package com.example.app.api.line.application;

import com.example.app.api.segment.api.dto.request.SegmentAttributeRequest;
import com.example.app.api.line.api.dto.request.line.CreateLineRequest;
import com.example.app.api.line.api.dto.request.line.UpdateLineAttributeRequest;
import com.example.app.api.line.api.dto.request.line.UpdateLineStatusRequest;
import com.example.app.common.dto.request.enums.ActionType;
import com.example.app.support.IntegrationTest;
import com.example.core.common.domain.enums.ActiveType;
import com.example.core.common.exception.DomainErrorCode;
import com.example.db.business.line.LineJpaEntity;
import com.example.app.common.exception.AppErrorCode;
import com.example.db.business.segment.SegmentJpaEntity;
import com.example.db.business.station.StationJpaEntity;
import com.example.db.support.ConcurrentRunner;
import com.example.db.support.DbHelper;
import com.example.core.common.exception.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;

class LineServiceTest extends IntegrationTest {

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
        ConcurrentRunner.Result result = ConcurrentRunner.run(threads, (i)-> {
            SegmentAttributeRequest seg = new SegmentAttributeRequest(1.0,2);
            CreateLineRequest req = new CreateLineRequest("line", 1, 2,seg);
            lineService.createLine(req);
        });

        // 성공 시
        assertEquals(1, dbHelper.countLineByName("line"));

        // 실패는 1개
        assertEquals(2, result.errorCount());

        List<CustomException> domainErrors = result.errorsOf(CustomException.class);
        assertEquals(2, domainErrors.size());
        domainErrors.forEach(ex ->
                assertEquals(DomainErrorCode.LINE_NAME_DUPLICATED, ex.getErrorCode())
        );
    }

    @Test
    @DisplayName("같은_역_동시_이름변경이면_한쪽은_낙관락_실패")
    void 같은_역_동시_이름변경이면_한쪽은_낙관락_실패() throws Exception {
        LineJpaEntity l = dbHelper.insertLineNoSegment("line 1");
        Integer id = l.getId();

        int threads = 2;
        ConcurrentRunner.Result result = ConcurrentRunner.run(threads, (i)-> {
            final String newName = (i == 0) ? "line A" : "line B";
            UpdateLineAttributeRequest req = new UpdateLineAttributeRequest(newName);
            lineService.updateLineName(id,req);
        });

        // 하나는 실패해야 한다
        assertEquals(1, result.errorCount());
        List<ObjectOptimisticLockingFailureException> domainErrors = result.errorsOf(ObjectOptimisticLockingFailureException.class);
        assertEquals(1, domainErrors.size());
    }

    @Test
    @DisplayName("동시에_같은_이름으로_수정시_하나는_200_하나는_에러_반환")
    void 동시에_같은_이름으로_수정시_하나는_200_하나는_에러_반환() throws Exception {
        LineJpaEntity l1 = dbHelper.insertLineNoSegment("line 1");
        LineJpaEntity l2 = dbHelper.insertLineNoSegment("line 2");
        Integer[] idArr = {l1.getId(), l2.getId()};

        int threads = 2;
        ConcurrentRunner.Result result = ConcurrentRunner.run(threads, (i)-> {
            String newName = "line A";
            lineService.updateLineName(idArr[i],
                    new UpdateLineAttributeRequest(newName));
        });

        // 성공 시
        assertEquals(1, dbHelper.countLineByName("line A"));

        // 실패는 1개
        assertEquals(1, result.errorCount());
        List<CustomException> domainErrors = result.errorsOf(CustomException.class);
        assertEquals(1, domainErrors.size());
        domainErrors.forEach(ex ->
                assertEquals(DomainErrorCode.LINE_NAME_DUPLICATED, ex.getErrorCode())
        );
    }

    @Test
    @DisplayName("라인_비활성화_정상_수행_테스트")
    void lineDeactivateTest() {
        StationJpaEntity s1 =  dbHelper.insertStation("station 1");
        StationJpaEntity s2 = dbHelper.insertStation("station 2");
        StationJpaEntity s3 = dbHelper.insertStation("station 3");
        StationJpaEntity s4 = dbHelper.insertStation("station 4");
        StationJpaEntity s5 = dbHelper.insertStation("station 5");
        LineJpaEntity l = dbHelper.insertLine("line 1", s1,s2,2.1, 3);
        SegmentJpaEntity sg1 = dbHelper.insertSegment(l,s2,s3,2.1,3, ActiveType.ACTIVE);
        SegmentJpaEntity sg2 = dbHelper.insertSegment(l,s3,s4,2.1,3, ActiveType.ACTIVE);
        SegmentJpaEntity sg3 = dbHelper.insertSegment(l,s4,s5,2.1,3, ActiveType.ACTIVE);
        UpdateLineStatusRequest req = new UpdateLineStatusRequest(ActionType.INACTIVE);
        lineService.updateLineStatus(l.getId(),req);
        assertEquals(4,dbHelper.countSnapshotSegmentsBySnapshotId(1));
    }

    @Test
    @DisplayName("라인_재활성화_정상_수행_테스트")
    void lineActivateTest() {
        StationJpaEntity s1 =  dbHelper.insertStation("station 1");
        StationJpaEntity s2 = dbHelper.insertStation("station 2");
        StationJpaEntity s3 = dbHelper.insertStation("station 3");
        StationJpaEntity s4 = dbHelper.insertStation("station 4");
        StationJpaEntity s5 = dbHelper.insertStation("station 5");
        LineJpaEntity l = dbHelper.insertLine("line 1", s1,s2,2.1, 3);
        SegmentJpaEntity sg0 = dbHelper.getSegmentById(1);
        SegmentJpaEntity sg1 = dbHelper.insertSegment(l,s2,s3,2.1,3, ActiveType.ACTIVE);
        SegmentJpaEntity sg2 = dbHelper.insertSegment(l,s3,s4,2.1,3, ActiveType.ACTIVE);
        SegmentJpaEntity sg3 = dbHelper.insertSegment(l,s4,s5,2.1,3, ActiveType.ACTIVE);
        // 비활성화
        UpdateLineStatusRequest req = new UpdateLineStatusRequest(ActionType.INACTIVE);
        lineService.updateLineStatus(l.getId(),req);
        // 재활성화
        req = new UpdateLineStatusRequest(ActionType.ACTIVE);
        lineService.updateLineStatus(l.getId(),req);
        assertEquals(4,dbHelper.countActiveSegmentByLineId(l.getId()));
        assertEquals(ActiveType.ACTIVE, dbHelper.getLineById(l.getId()).getActiveType());
    }

    @Test
    @DisplayName("동시에_같은_상태로_수정시_모두_성공")
    void lineStatusConcurrentTest() throws Exception {
        StationJpaEntity s1 =  dbHelper.insertStation("station 1");
        StationJpaEntity s2 = dbHelper.insertStation("station 2");
        StationJpaEntity s3 = dbHelper.insertStation("station 3");
        StationJpaEntity s4 = dbHelper.insertStation("station 4");
        StationJpaEntity s5 = dbHelper.insertStation("station 5");
        LineJpaEntity l = dbHelper.insertLine("line 1", s1,s2,2.1, 3);
        SegmentJpaEntity sg0 = dbHelper.getSegmentById(1);
        SegmentJpaEntity sg1 = dbHelper.insertSegment(l,s2,s3,2.1,3, ActiveType.ACTIVE);
        SegmentJpaEntity sg2 = dbHelper.insertSegment(l,s3,s4,2.1,3, ActiveType.ACTIVE);
        SegmentJpaEntity sg3 = dbHelper.insertSegment(l,s4,s5,2.1,3, ActiveType.ACTIVE);

        int threads = 2;
        ConcurrentRunner.Result result = ConcurrentRunner.run(threads, (i)-> {
            UpdateLineStatusRequest req = new UpdateLineStatusRequest(ActionType.INACTIVE);
            lineService.updateLineStatus(l.getId(),req);
        });

        // 모두 성공
        assertEquals(0, result.errorCount());
        LineJpaEntity reloaded = dbHelper.getLineById(l.getId());
        assertEquals(ActiveType.INACTIVE, reloaded.getActiveType());
    }

    static Stream<Arguments> NotFoundStationId() {
        return Stream.of(
                Arguments.of(10, 1),
                Arguments.of(1, 10),
                Arguments.of(0, 1)
        );
    }
}