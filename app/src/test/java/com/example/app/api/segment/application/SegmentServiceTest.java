package com.example.app.api.segment.application;

import com.example.app.api.line.api.dto.request.SegmentAttributeRequest;
import com.example.app.api.line.api.dto.request.line.UpdateLineAttributeRequest;
import com.example.app.api.segment.api.dto.request.UpdateSegmentAttributeRequest;
import com.example.app.support.IntegrationTest;
import com.example.core.domain.segment.SegmentAttribute;
import com.example.db.business.line.LineJpaEntity;
import com.example.db.business.segment.SegmentJpaEntity;
import com.example.db.business.station.StationJpaEntity;
import com.example.db.support.ConcurrentRunner;
import com.example.db.support.DbHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SegmentServiceTest extends IntegrationTest {

    @Autowired SegmentService segmentService;
    @Autowired
    DbHelper dbHelper;

    @BeforeEach
    void clean() {
        dbHelper.truncateAll();
    }

    @Test
    @DisplayName("같은 segment 동시에 수정시 하나만 정상적으로 수행")
    void updateSegmentAttributeConcurrencyTest() throws Exception{
        StationJpaEntity s1 =  dbHelper.insertStation("station 1");
        StationJpaEntity s2 = dbHelper.insertStation("station 2");
        LineJpaEntity l = dbHelper.insertLine("line 1", s1, s2, 1.1, 2);
        SegmentJpaEntity sg = dbHelper.getSegmentById(1);

        SegmentAttributeRequest[] attributes = {
                new SegmentAttributeRequest(1.0, 5),
                new SegmentAttributeRequest(2.0, 100),
                new SegmentAttributeRequest(3.0, 100),
                new SegmentAttributeRequest(4.0, 100),
                new SegmentAttributeRequest(5.0, 100)
        };

        int threads = 5;
        ConcurrentRunner.Result result = ConcurrentRunner.run(threads, (i)-> {
            UpdateSegmentAttributeRequest req = new UpdateSegmentAttributeRequest(attributes[i]);
            segmentService.updateSegmentAttribute(sg.getId(), req);
        });

        // 나머지 낙관락 실패
        assertEquals(4, result.errorCount());
        List<ObjectOptimisticLockingFailureException> domainErrors = result.errorsOf(ObjectOptimisticLockingFailureException.class);
        assertEquals(4, domainErrors.size());
    }

}