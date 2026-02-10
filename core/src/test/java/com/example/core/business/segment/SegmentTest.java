package com.example.core.business.segment;

import com.example.core.exception.CustomException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;

class SegmentTest {

    @Test
    @DisplayName("before와 after역은 달라야한다.")
    void before와_after역은_달라야한다() {
        SegmentAttribute attribute = new SegmentAttribute(1.4,2);
        assertThatThrownBy(()->Segment.create(1,2,2,attribute))
                .isInstanceOf(CustomException.class);
    }

    @ParameterizedTest
    @MethodSource("nullSegmentAttributes")
    void segment_속성은_null이어서는_안된다(Double distance, Integer spendTime) {
        assertThatThrownBy(()->new SegmentAttribute(distance, spendTime))
                .isInstanceOf(CustomException.class);
    }

    @ParameterizedTest
    @MethodSource("invalidSegmentAttributes")
    @DisplayName("segment_속성값들은_0보다_커야한다.")
    void segment_속성값들은_0보다_커야한다(Double distance, Integer spendTime) {
        assertThatThrownBy(()->new SegmentAttribute(distance, spendTime))
                .isInstanceOf(CustomException.class);
    }

    static Stream<Arguments> invalidSegmentAttributes() {
        return Stream.of(
                Arguments.of(0.0, 10),
                Arguments.of(10.0, 0),
                Arguments.of(-1.4, 5),
                Arguments.of(-0.001, 1)
        );
    }

    static Stream<Arguments> nullSegmentAttributes() {
        return Stream.of(
                Arguments.of(null, 10),
                Arguments.of(10.0, null),
                Arguments.of(null, null)
        );
    }
}