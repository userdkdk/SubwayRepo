package com.example.core.business.line;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class LineTest {

    @Test
    @DisplayName("노선 이름 규칙에 맞는 이름을 입력해야 노선이 생성된다.")
    void createByValidName() {
        LineName name = new LineName("테스트 노선");

    }

    @Test
    @DisplayName("이름은_빈값이거나_null이어서는_안된다")
    void 이름은_빈값이거나_null이어서는_안된다() {

    }

    @Test
    @DisplayName("아름의_길이는_2~15자여야_한다")
    void 이름의_길이는_2에서_15자여야_한다() {

    }

    @Test
    @DisplayName("이름엔_특수문자만_허용된다")
    void 이름엔_특수문자만_허용된다() {

    }
}