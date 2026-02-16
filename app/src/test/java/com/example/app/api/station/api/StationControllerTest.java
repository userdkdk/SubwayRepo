package com.example.app.api.station.api;

import com.example.app.api.station.application.StationService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = StationController.class)
class StationControllerTest {
    @Autowired protected MockMvc mvc;
    @MockitoBean private StationService stationService;

    @Test
    @DisplayName("name_null이면_400_반환")
    void name_null이면_400_반환() throws Exception {
        mvc.perform(post("/api/stations")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":null}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("name_blank이면_400_반환")
    void name_blank이면_400_반환() throws Exception {
        mvc.perform(post("/api/stations")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"   \"}"))
                .andExpect(status().isBadRequest());
    }
}