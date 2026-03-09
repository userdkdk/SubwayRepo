package com.example.app.api.segment.application;

import com.example.app.support.IntegrationTest;
import com.example.db.support.DbHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

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
    @DisplayName("같은_이름_동시_생성시_하나는_저장_하나는_에러_반환")
    void test() {

    }

}