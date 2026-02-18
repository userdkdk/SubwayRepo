package com.example.core.business.line;

import com.example.core.exception.CustomException;
import com.example.core.exception.DomainErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class LineTest {

    @ParameterizedTest
    @ValueSource(strings = {"테스트 노선", "테스트 노선", "테스트-노선", "Line 1", "테스트     노선"})
    @DisplayName("정상 이름을 입력해야 노선이 생성된다.")
    void createByValidName(String input) {
        LineName name = new LineName(input);
        assertThat(name.value()).isEqualTo(input);

        LineName n2 = new LineName("Line 1");
        assertThat(n2.value()).isNotEqualTo("line 1");
    }

    @Test
    @DisplayName("이름은_빈값이거나_null이어서는_안된다")
    void 이름은_빈값이거나_null이어서는_안된다() {
        assertThatThrownBy(() -> new LineName(null))
                .isInstanceOf(CustomException.class);
        assertThatThrownBy(() -> new LineName(""))
                .isInstanceOf(CustomException.class);
        assertThatThrownBy(() -> new LineName("    "))
                .isInstanceOf(CustomException.class);
    }

    @ParameterizedTest
    @ValueSource(strings = {"a", "  a  ", "1234567890123456",
            "abcdefghijklmnopq"})
    @DisplayName("이름의_길이는_2~15자여야_한다")
    void 이름의_길이는_2에서_15자여야_한다(String input) {
        assertThatThrownBy(()->new LineName(input))
                .isInstanceOf(CustomException.class)
                .extracting("errorCode")
                .isEqualTo(DomainErrorCode.LINE_NAME_ERROR);
    }

    @ParameterizedTest
    @ValueSource(strings = {"a_b", "a!b", "-abc","abc-","ab cd $e"})
    @DisplayName("이름엔_지정된_특수문자만_허용된다")
    void 이름엔_특수문자만_허용된다(String input) {
        assertThatThrownBy(()->new LineName(input))
                .isInstanceOf(CustomException.class)
                .extracting("errorCode")
                .isEqualTo(DomainErrorCode.LINE_NAME_ERROR);
    }
}