package com.example.app.business.station;

import com.example.app.common.exception.AppErrorCode;
import com.example.app.support.DbHelper;
import com.example.app.support.MySqlFlywayTcConfig;
import com.example.core.business.station.Station;
import com.example.core.business.station.StationName;
import com.example.core.exception.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.assertj.core.api.Assertions.*;

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
    @DisplayName("정상적인_경우_이름을_업데이트_할_수_있다")
    void 정상적인_경우_이름을_업데이트_할_수_있다() {
        StationJpaEntity s1 = dbHelper.insertStation("station 1");
        dbHelper.insertStation("station 2");

        stationAdapter.update(s1.getId(), station -> {
            StationName name = new StationName("station 1-new");
            station.changeName(name);
        });

        StationJpaEntity reloaded = dbHelper.getStationById(s1.getId());
        assertEquals("station 1-new", reloaded.getName());
    }

    @ParameterizedTest
    @ValueSource(strings = {"station 1", "Station 1", "STATION 1"})
    @DisplayName("중복된_이름이_있는_경우_이름을_업데이트_할_수_없다")
    void 중복된_이름이_있는_경우_이름을_업데이트_할_수_없다(String input) {
        dbHelper.insertStation("station 1");

        assertThatThrownBy(()->dbHelper.insertStation(input))
                .isInstanceOf(DataIntegrityViolationException.class);

        Station station = Station.create(new StationName("  station 1 "));
        assertThatThrownBy(()->stationAdapter.save(station))
                .isInstanceOf(DataIntegrityViolationException.class);

    }

    @Test
    void existsActiveById() {
    }
}