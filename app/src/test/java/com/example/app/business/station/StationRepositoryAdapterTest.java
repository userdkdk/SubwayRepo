package com.example.app.business.station;

import com.example.app.common.exception.AppErrorCode;
import com.example.app.support.DbHelper;
import com.example.app.support.MySqlFlywayTcConfig;
import com.example.core.common.domain.enums.ActiveType;
import com.example.core.exception.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
@Import({StationRepositoryAdapter.class, DbHelper.class, StationMapper.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class StationRepositoryAdapterTest extends MySqlFlywayTcConfig {

    @Autowired StationRepositoryAdapter stationAdapter;
    @Autowired TestEntityManager em;
    @Autowired DbHelper dbHelper;

    @BeforeEach
    void clean() {
        dbHelper.truncateAll();
    }

    @Test
    void save() {
    }

    @Test
    @DisplayName("업데이트 확인")
    void update_success() {
        StationJpaEntity s1 = dbHelper.insertStation("station 1");

        stationAdapter.update(s1.getId(), station -> {
            station.changeName("station 1-new");
            station.changeActiveType(ActiveType.INACTIVE);
        });

        StationJpaEntity reloaded = dbHelper.getStationById(s1.getId());
        assertEquals("station 1-new", reloaded.getName());
        assertEquals(ActiveType.INACTIVE, reloaded.getActiveType());
    }

    @Test
    @DisplayName("업데이트시 이름 같은 경우 실패")
    void updateDuplicatedName() {
        StationJpaEntity s1 = dbHelper.insertStation("station 1");
        dbHelper.insertStation("station 2");

        CustomException ex = assertThrows(CustomException.class, () ->
                stationAdapter.update(s1.getId(), st -> st.changeName("station 2"))
        );

        assertEquals(AppErrorCode.STATION_NAME_DUPLICATED, ex.getErrorCode());
    }

    @Test
    void existsActiveById() {
    }
}