package com.example.app.api.segment.api;

import com.example.app.api.segment.application.SegmentService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.stream.Stream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = SegmentController.class)
class SegmentControllerTest {
    @Autowired
    protected MockMvc mvc;
    @MockitoBean private SegmentService segmentService;

    @ParameterizedTest(name = "{0}")
    @MethodSource("invalidSegmentUpdateRequest")
    @DisplayName("segment_attribute 업데이트시 attribute 문제있으면 에러 반환")
    void invalidSegmentUpdateRequestTest(String displayName, String req) throws Exception {
        mvc.perform(patch("/api/segments/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(req))
                .andExpect(status().isBadRequest());
    }

    static Stream<Arguments> invalidSegmentUpdateRequest() {
        return Stream.of(
                Arguments.of("attribute null", "{\"attribute\":null}"),
                Arguments.of("attribute 누락", "{}"),
                Arguments.of("attribute status null", "{\"attribute\":{\"distance\":null,\"spendTime\":10}}"),
                Arguments.of("attribute status not positive", "{\"attribute\":{\"distance\":0.0,\"spendTime\":10}}")
        );
    }
}