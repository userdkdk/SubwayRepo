package com.example.app.api.line.api;

import com.example.app.api.line.api.dto.request.SegmentAttributeRequest;
import com.example.app.api.line.api.dto.request.line.CreateLineRequest;
import com.example.app.api.line.application.LineService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = LineController.class)
class LineControllerTest {
    @Autowired protected MockMvc mvc;
    @MockitoBean private LineService lineService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @ParameterizedTest(name = "{0}")
    @MethodSource("invalidCreateLineRequests")
    @DisplayName("line_create_validation_실패하면_400")
    void line_create_validation_실패하면_400(String displayName, CreateLineRequest req) throws Exception {
        mvc.perform(post("/api/lines")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest());
    }

    static Stream<Arguments> invalidCreateLineRequests() {
        SegmentAttributeRequest validSeg = new SegmentAttributeRequest(1.2, 3);

        return Stream.of(
                Arguments.of("name blank", new CreateLineRequest("  ", 1, 2, validSeg)),
                Arguments.of("beforeId null", new CreateLineRequest("line 1", null, 2, validSeg)),
                Arguments.of("afterId null", new CreateLineRequest("line 1", 1, null, validSeg)),
                Arguments.of("seg distance negative", new CreateLineRequest("line 1", 1, 2, new SegmentAttributeRequest(-1.2, 3))),
                Arguments.of("seg time negative", new CreateLineRequest("line 1", 1, 2, new SegmentAttributeRequest(1.2, -3)))
        );
    }
}