package com.example.app.api.station.api;

import com.example.app.api.station.api.dto.response.StationDetailResponse;
import com.example.app.api.station.api.dto.response.StationResponse;
import com.example.app.api.station.application.StationViewService;
import com.example.app.common.dto.request.ViewRequestFilter;
import com.example.app.common.dto.page.CustomPage;
import com.example.app.common.dto.response.CustomResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/stations")
@RequiredArgsConstructor
public class StationViewController {

    private final StationViewService stationViewService;

    // get stations by filter
    @GetMapping("")
    public ResponseEntity<CustomResponse<CustomPage<StationResponse>>> getStations(
            @ModelAttribute @Valid ViewRequestFilter request
    ) {
        return CustomResponse.ok(stationViewService.getStations(request));
    }

    @GetMapping("/{stationId}")
    public ResponseEntity<CustomResponse<StationDetailResponse>> getStationById(
            @PathVariable Integer stationId
    ) {
        StationDetailResponse res = stationViewService.getStationById(stationId);
        return CustomResponse.ok(res);
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<CustomResponse<StationDetailResponse>> getStationByName(
            @PathVariable String name
    ) {
        StationDetailResponse res = stationViewService.getStationByName(name);
        return CustomResponse.ok(res);
    }
}
