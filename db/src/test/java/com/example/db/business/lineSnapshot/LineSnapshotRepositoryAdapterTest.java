package com.example.db.business.lineSnapshot;

import com.example.core.common.domain.enums.ActiveType;
import com.example.db.business.line.LineJpaEntity;
import com.example.db.business.segment.SegmentJpaEntity;
import com.example.db.business.station.StationJpaEntity;
import com.example.db.business.station.StationMapper;
import com.example.db.business.station.StationRepositoryAdapter;
import com.example.db.support.DbHelper;
import com.example.db.support.MySqlFlywayTcConfig;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({LineSnapshotRepositoryAdapter.class, DbHelper.class, LineSnapshotMapper.class})
class LineSnapshotRepositoryAdapterTest extends MySqlFlywayTcConfig {
    @Autowired LineSnapshotRepositoryAdapter snapshotRepo;
    @Autowired EntityManager em;
    @Autowired DbHelper dbHelper;

    @BeforeEach
    void clean() {
        dbHelper.truncateAll();
    }

    @Test
    @DisplayName("정상적인_경우_snapshot의모든 seg를 반환한다")
    void returnAllSegmentsOfSnapshot() {
        StationJpaEntity s1 =  dbHelper.insertStation("station 1");
        StationJpaEntity s2 = dbHelper.insertStation("station 2");
        StationJpaEntity s3 = dbHelper.insertStation("station 3");
        StationJpaEntity s4 = dbHelper.insertStation("station 4");
        StationJpaEntity s5 = dbHelper.insertStation("station 5");
        LineJpaEntity l = dbHelper.insertLine("line 1", s1,s2,2.1, 3);
        SegmentJpaEntity sg1 = dbHelper.insertSegment(l,s2,s3,2.1,3, ActiveType.INACTIVE);
        SegmentJpaEntity sg2 = dbHelper.insertSegment(l,s3,s4,2.1,3, ActiveType.INACTIVE);
        SegmentJpaEntity sg3 = dbHelper.insertSegment(l,s4,s5,2.1,3, ActiveType.INACTIVE);
        LineSnapshotJpaEntity snapshot = dbHelper.insertSnapshot(l.getId());
        l.changeActiveType(ActiveType.INACTIVE);
        dbHelper.insertSnapshotSegment(snapshot.getId(), sg1.getId());
        dbHelper.insertSnapshotSegment(snapshot.getId(), sg2.getId());
        dbHelper.insertSnapshotSegment(snapshot.getId(), sg3.getId());
        em.flush();
        List<Integer> segsId = snapshotRepo.findSegsIdByLine(l.getId());
        assertEquals(segsId.size(), 3);
    }
}