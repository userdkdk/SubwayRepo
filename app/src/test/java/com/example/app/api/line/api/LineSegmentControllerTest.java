package com.example.app.api.line.api;

import com.example.app.api.line.api.dto.request.SegmentAttributeRequest;
import com.example.app.api.line.api.dto.request.line.CreateLineRequest;
import com.example.app.api.line.api.dto.request.segment.CreateSegmentRequest;
import com.example.app.api.line.application.LineSegmentService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = LineSegmentController.class)
class LineSegmentControllerTest {
    @Autowired protected MockMvc mvc;
    @MockitoBean private LineSegmentService lineSegmentService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @ParameterizedTest(name = "{0}")
    @MethodSource("validCreateSegmentRequests")
    @DisplayName("create segment request 검증")
    void validCreateSegmentRequestTest(String displayName, CreateSegmentRequest req) throws Exception {
        mvc.perform(post("/api/lines/1/stations/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated());
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("invalidCreateSegmentRequests")
    @DisplayName("invalid create segment request 검증")
    void invalidCreateSegmentRequestTest(String displayName, CreateSegmentRequest req) throws Exception {
        mvc.perform(post("/api/lines/1/stations/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest());
    }

    static Stream<Arguments> validCreateSegmentRequests() {
        SegmentAttributeRequest validSeg = new SegmentAttributeRequest(1.2, 3);

        return Stream.of(
                Arguments.of("before id blank", new CreateSegmentRequest(null,1,validSeg,validSeg)),
                Arguments.of("after id blank", new CreateSegmentRequest(1,null,validSeg,validSeg)),
                Arguments.of("after seg blank", new CreateSegmentRequest(1,null,validSeg,null)),
                Arguments.of("all seg blank", new CreateSegmentRequest(1,null,null,null))
        );
    }

    static Stream<Arguments> invalidCreateSegmentRequests() {
        SegmentAttributeRequest validSeg = new SegmentAttributeRequest(1.2, 3);
        SegmentAttributeRequest invalidSeg1 = new SegmentAttributeRequest(null, 3);
        SegmentAttributeRequest invalidSeg2 = new SegmentAttributeRequest(-1.2, 3);

        return Stream.of(
                Arguments.of("null in seg attribute", new CreateSegmentRequest(null,1,validSeg,invalidSeg1)),
                Arguments.of("negative in seg attribute", new CreateSegmentRequest(1,null,validSeg,invalidSeg2))
        );
    }
}