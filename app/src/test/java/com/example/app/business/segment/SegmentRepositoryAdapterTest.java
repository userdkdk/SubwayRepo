package com.example.app.business.segment;

import com.example.app.business.line.LineJpaEntity;
import com.example.app.business.station.StationJpaEntity;
import com.example.app.support.DbHelper;
import com.example.app.support.MySqlFlywayTcConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import({SegmentRepositoryAdapter.class, SegmentMapper.class, DbHelper.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class SegmentRepositoryAdapterTest extends MySqlFlywayTcConfig {

    @Autowired SegmentRepositoryAdapter adapter;
    @Autowired TestEntityManager em;
    @Autowired DbHelper dbHelper;

    @BeforeEach
    void clean() {
        dbHelper.truncateAll();
    }

    @Test
    void existsActiveStationInLine() {
    }

    @Test
    void existsActiveStation() {
        StationJpaEntity s1 = dbHelper.insertStation("station 1");
        StationJpaEntity s2 = dbHelper.insertStation("station 2");
        LineJpaEntity line = dbHelper.insertLine("line 1");
        dbHelper.insertSegment(line, s1, s2, 3.3, 4);

        em.flush();
        em.clear();

        assertTrue(adapter.existsActiveSegmentByStation(s1.getId()));
    }

    @Test
    void findActiveRole() {
    }

    @Test
    void inactivateActiveSegment() {
    }
}