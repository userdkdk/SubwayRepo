package com.example.app.business.line;

import com.example.app.common.exception.AppErrorCode;
import com.example.app.support.DbHelper;
import com.example.app.support.MySqlFlywayTcConfig;
import com.example.core.business.line.Line;
import com.example.core.business.line.LineName;
import com.example.core.exception.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
@Import({LineRepositoryAdapter.class, LineMapper.class, DbHelper.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class LineRepositoryAdapterTest extends MySqlFlywayTcConfig {

    @Autowired LineRepositoryAdapter lineRepo;
    @Autowired
    TestEntityManager em;
    @Autowired DbHelper dbHelper;

    @BeforeEach
    void clean() {
        dbHelper.truncateAll();
    }

    @ParameterizedTest
    @ValueSource(strings = {"line 1", "LINE 1", "Line 1"})
    @DisplayName("이름 조회후 중복이름 존재하면 로직에서 에러 반환")
    void 이름_조회후_중복이름_존재하면_로직에서_에러_반환(String input) {
        dbHelper.insertLineNoSegment("line 1");

        assertThatThrownBy(() -> lineRepo.ensureNameUnique(input))
                .isInstanceOf(CustomException.class)
                .extracting("errorCode")
                .isEqualTo(AppErrorCode.LINE_NAME_DUPLICATED);
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
                .isEqualTo(AppErrorCode.LINE_NAME_DUPLICATED);
    }

}