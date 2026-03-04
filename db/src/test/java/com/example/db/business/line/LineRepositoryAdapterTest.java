package com.example.db.business.line;

import com.example.core.business.line.Line;
import com.example.core.business.line.LineName;
import com.example.core.common.exception.CustomException;
import com.example.core.common.exception.DomainErrorCode;
import com.example.db.support.DbHelper;
import com.example.db.support.MySqlFlywayTcConfig;
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

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({LineRepositoryAdapter.class, LineMapper.class, DbHelper.class})
class LineRepositoryAdapterTest extends MySqlFlywayTcConfig {

    @Autowired
    LineRepositoryAdapter lineRepo;
    @Autowired
    TestEntityManager em;
    @Autowired DbHelper dbHelper;

    @BeforeEach
    void clean() {
        dbHelper.truncateAll();
    }

    @ParameterizedTest
    @ValueSource(strings = {"line 1", "LINE 1", "Line 1"})
    @DisplayName("DB에 직접 저장시 중복 이름 존재하면 에러 반환")
    void DB에_직접_저장시_중복_이름_존재하면_에러_반환(String input) {
        dbHelper.insertLineNoSegment("line 1");

        assertThatThrownBy(() -> dbHelper.insertLineNoSegment(input))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @ParameterizedTest
    @ValueSource(strings = {"line 1", "LINE 1", "Line 1"})
    @DisplayName("저장_로직에서_이름_중복이면_에러_반환")
    void 저장_로직에서_이름_중복이면_에러_반환(String input) {
        dbHelper.insertLineNoSegment("line 1");
        Line line = Line.create(new LineName(input));

        assertThatThrownBy(() -> lineRepo.save(line))
                .isInstanceOf(CustomException.class)
                .extracting("errorCode")
                .isEqualTo(DomainErrorCode.LINE_NAME_DUPLICATED);
    }

    @Test
    @DisplayName("정상적인_경우_이름을_업데이트_할_수_있다")
    void 정상적인_경우_이름을_업데이트_할_수_있다() {
        LineJpaEntity l = dbHelper.insertLineNoSegment("line 1");

        lineRepo.updateAttribute(l.getId(),new LineName("line 2"));

        LineJpaEntity reloaded = dbHelper.getLineById(l.getId());
        assertEquals("line 2", reloaded.getName());
    }

    @Test
    @DisplayName("업데이트시_중복_이름_존재하면_에러_반환")
    void 업데이트시_중복_이름_존재하면_에러_반환() {
        LineJpaEntity l1 = dbHelper.insertLineNoSegment("line 1");
        LineJpaEntity l2 = dbHelper.insertLineNoSegment("line 2");

        assertThatThrownBy(()->lineRepo.updateAttribute(l1.getId(),new LineName("line 2")))
                .isInstanceOf(CustomException.class)
                .extracting("errorCode")
                .isEqualTo(DomainErrorCode.LINE_NAME_DUPLICATED);
    }

}