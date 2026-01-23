package com.example.app.api.station.api;

import com.example.app.api.station.api.dto.response.StationResponse;
import com.example.app.api.station.application.StationViewService;
import com.example.app.common.response.CustomResponse;
import com.example.app.common.response.enums.StatusFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/station")
@RequiredArgsConstructor
public class StationViewController {

    private final StationViewService stationViewService;

    // get stations by activeType
    @GetMapping("")
    public ResponseEntity<CustomResponse<List<StationResponse>>> getStations(
            @RequestParam(defaultValue = "ACTIVE") StatusFilter status
    ) {
        return CustomResponse.ok(stationViewService.getStations(status));
    }

    @GetMapping("/{stationId}")
    public ResponseEntity<CustomResponse<StationResponse>> getStation(
            @PathVariable Integer stationId
    ) {
        StationResponse res = stationViewService.getStationById(stationId);
        return CustomResponse.ok(res);
    }
}
