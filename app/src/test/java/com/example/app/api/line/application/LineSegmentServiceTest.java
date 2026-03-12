package com.example.app.api.line.application;

import com.example.app.api.segment.api.dto.request.SegmentAttributeRequest;
import com.example.app.api.line.api.dto.request.segment.CreateSegmentRequest;
import com.example.app.common.exception.AppErrorCode;
import com.example.app.support.IntegrationTest;
import com.example.core.common.exception.CustomException;
import com.example.core.common.exception.ErrorCode;
import com.example.db.business.line.LineJpaEntity;
import com.example.db.business.segment.SegmentRepositoryAdapter;
import com.example.db.business.station.StationJpaEntity;
import com.example.db.support.ConcurrentRunner;
import com.example.db.support.DbHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;

class LineSegmentServiceTest extends IntegrationTest {

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

    @Test
    @DisplayName("동시에 서로 충돌이 나는 요청이 오면 하나는 정상 수행, 하나는 에러 반환")
    void addDifferentStationConcurrentTest() throws Exception {
        StationJpaEntity s1 = dbHelper.insertStation("station 1");
        StationJpaEntity s2 = dbHelper.insertStation("station 2");
        StationJpaEntity s3 = dbHelper.insertStation("station 3");
        StationJpaEntity s4 = dbHelper.insertStation("station 4");
        LineJpaEntity l = dbHelper.insertLine("line", s1, s2, 1.2, 3);

        int threads = 2;
        Integer[] idArr = {s3.getId(), s4.getId()};
        ConcurrentRunner.Result result = ConcurrentRunner.run(threads, (i)-> {
            SegmentAttributeRequest seg = new SegmentAttributeRequest(1.0,2);
            CreateSegmentRequest req = new CreateSegmentRequest(s1.getId(), s2.getId(), seg, seg);
            lineSegmentService.addStation(l.getId(), idArr[i], req);
        });

        // 실패는 1개, SEGMENT_NOT_FOUND 반환
        List<CustomException> domainErrors = result.errorsOf(CustomException.class);
        assertEquals(1, domainErrors.size());
        domainErrors.forEach(ex ->
                assertEquals(AppErrorCode.SEGMENT_NOT_FOUND, ex.getErrorCode())
        );

        // 최종 seg는 2개
        assertEquals(2,dbHelper.countActiveSegmentByLineId(l.getId()));
    }

    @Test
    @DisplayName("동시에 충돌이 나지 않는 요청이 오면 정상 수행")
    void addStationConcurrentTest() throws Exception {
        StationJpaEntity s1 = dbHelper.insertStation("station 1");
        StationJpaEntity s2 = dbHelper.insertStation("station 2");
        StationJpaEntity s3 = dbHelper.insertStation("station 3");
        StationJpaEntity s4 = dbHelper.insertStation("station 4");
        LineJpaEntity l = dbHelper.insertLine("line", s1, s2, 1.2, 3);

        int threads = 2;
        Integer[] idArr = {s3.getId(), s4.getId()};
        ConcurrentRunner.Result result = ConcurrentRunner.run(threads, (i)-> {
            SegmentAttributeRequest seg = new SegmentAttributeRequest(1.0,2);
            final CreateSegmentRequest req = (i==0) ?
                    new CreateSegmentRequest(s2.getId(), null, seg, seg)
                    : new CreateSegmentRequest(null, s1.getId(), seg, seg);
            lineSegmentService.addStation(l.getId(), idArr[i], req);
        });

        // 실패는 없음
        assertEquals(0, result.errorCount());

        // 최종 seg는 3개
        assertEquals(3,dbHelper.countActiveSegmentByLineId(l.getId()));
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("invalidStationInputRequest")
    @DisplayName("주어진 역 정보와 실제 역 정보가 다를때 에러 반환")
    void invalidInputStationInfoTest(String displayName, CreateSegmentRequest req, ErrorCode code) {
        StationJpaEntity s1 = dbHelper.insertStation("station 1");
        StationJpaEntity s2 = dbHelper.insertStation("station 2");
        StationJpaEntity s3 = dbHelper.insertStation("station 3");
        StationJpaEntity s4 = dbHelper.insertStation("station 4");
        LineJpaEntity l = dbHelper.insertLine("line", s1, s2, 1.2, 3);

        assertThatThrownBy(()->lineSegmentService.addStation(l.getId(), s3.getId(), req))
                .isInstanceOf(CustomException.class)
                .extracting("errorCode")
                .isEqualTo(code);

    }


    static Stream<Arguments> invalidStationInputRequest() {
        SegmentAttributeRequest validSeg = new SegmentAttributeRequest(1.2, 3);

        return Stream.of(
                Arguments.of("not head",
                        new CreateSegmentRequest(null, 2, validSeg, validSeg),
                        AppErrorCode.SEGMENT_INPUT_VALUE_ERROR),
                Arguments.of("not tail",
                        new CreateSegmentRequest(1, null, validSeg, validSeg),
                        AppErrorCode.SEGMENT_INPUT_VALUE_ERROR),
                Arguments.of("not internal",
                        new CreateSegmentRequest(2, 1, validSeg, validSeg),
                        AppErrorCode.SEGMENT_NOT_FOUND)
        );
    }
}