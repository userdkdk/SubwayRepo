package com.example.app.api.station.application;

import com.example.app.api.station.api.dto.request.CreateStationRequest;
import com.example.app.support.IntegrationTest;
import com.example.db.business.station.StationJpaEntity;
import com.example.db.support.DbHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StationIntegrationTest extends IntegrationTest {

    @Autowired
    StationService stationService;
    @Autowired
    DbHelper dbHelper;

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


}
