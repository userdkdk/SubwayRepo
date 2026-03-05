package com.example.db.business.station;

import com.example.core.domain.station.Station;
import com.example.core.domain.station.StationName;
import com.example.core.common.exception.CustomException;
import com.example.core.common.exception.DomainErrorCode;
import com.example.db.support.DbHelper;
import com.example.db.support.MySqlFlywayTcConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({StationRepositoryAdapter.class, StationMapper.class, DbHelper.class})
class StationRepositoryAdapterTest extends MySqlFlywayTcConfig {
    @Autowired
    StationRepositoryAdapter stationAdapter;
    @Autowired
    TestEntityManager em;
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

    @Test
    @DisplayName("DB에 직접 저장시 중복 이름 존재하면 에러 반환")
    void DB에_직접_저장시_중복_이름_존재하면_에러_반환() {
        dbHelper.insertStation("station 1");

        assertThatThrownBy(() -> dbHelper.insertStation("station 1"))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    @DisplayName("저장_로직에서_이름_중복이면_에러_반환")
    void 저장_로직에서_이름_중복이면_에러_반환() {
        dbHelper.insertStation("station 1");
        Station station = Station.create(new StationName("station 1"));

        assertThatThrownBy(() -> stationAdapter.save(station))
                .isInstanceOf(CustomException.class)
                .extracting("errorCode")
                .isEqualTo(DomainErrorCode.STATION_NAME_DUPLICATED);
    }

    @Test
    @DisplayName("업데이트시_중복_이름_존재하면_에러_반환")
    void 업데이트시_중복_이름_존재하면_에러_반환() {
        StationJpaEntity s1 = dbHelper.insertStation("station 1");
        StationJpaEntity s2 = dbHelper.insertStation("station 2");

        assertThatThrownBy(() -> stationAdapter.update(s2.getId(), st -> st.changeName(new StationName("station 1"))))
                .isInstanceOf(CustomException.class)
                .extracting("errorCode")
                .isEqualTo(DomainErrorCode.STATION_NAME_DUPLICATED);
    }

    @Test
    @DisplayName("같은 이름으로 변경시 flush 여부 확인")
    void checkVersionUpdateToSameName() {
        StationJpaEntity s1 = dbHelper.insertStation("station 1");
        stationAdapter.update(s1.getId(), st -> st.changeName(new StationName("station 1")));
        StationJpaEntity reload = dbHelper.getStationById(1);
        assertEquals(reload.getName(),"station 1");
        assertEquals(reload.getVersion(),0);
    }
}