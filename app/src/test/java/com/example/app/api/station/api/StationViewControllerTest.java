package com.example.app.api.station.api;

import com.example.app.api.station.application.StationViewService;
import com.example.app.common.dto.request.ViewRequestFilter;
import com.example.app.common.dto.request.enums.SortType;
import com.example.app.common.dto.request.enums.StatusFilter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = StationViewController.class)
class StationViewControllerTest {
    @Autowired
    protected MockMvc mvc;
    @MockitoBean private StationViewService service;

    @Test
    @DisplayName("기본값 조회 테스트")
    void defaultFilterCheckTest() throws Exception {
        mvc.perform(get("/api/stations"))
                .andExpect(status().isOk());
        ArgumentCaptor<ViewRequestFilter> captor =
                ArgumentCaptor.forClass(ViewRequestFilter.class);
        verify(service).getStations(captor.capture());

        ViewRequestFilter request = captor.getValue();
        assertEquals(StatusFilter.ACTIVE, request.status());
        assertEquals(SortType.ID, request.sortType());
        assertEquals(Sort.Direction.ASC, request.direction());
        assertEquals(0, request.page());
        assertEquals(10, request.size());
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("invalidFilter")
    @DisplayName("segment_attribute 업데이트시 attribute 문제있으면 에러 반환")
    void invalidSegmentUpdateRequestTest(String displayName, String param, String value) throws Exception {
        mvc.perform(get("/api/stations")
                        .param(param, value))
                .andExpect(status().isBadRequest());
    }

    static Stream<Arguments> invalidFilter() {
        return Stream.of(
                Arguments.of("status wrong", "status", "WRONG"),
                Arguments.of("sortType wrong", "sortType", "WRONG"),
                Arguments.of("direction wrong", "direction", "WRONG"),
                Arguments.of("page invalid", "page", "-1"),
                Arguments.of("size invalid", "size", "0")
        );
    }

}