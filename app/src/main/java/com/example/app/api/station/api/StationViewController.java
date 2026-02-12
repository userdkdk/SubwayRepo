package com.example.app.api.station.api;

import com.example.app.api.station.api.dto.response.StationDetailResponse;
import com.example.app.api.station.api.dto.response.StationSummaryResponse;
import com.example.app.api.station.application.StationViewService;
import com.example.app.common.dto.response.CustomPage;
import com.example.app.common.dto.response.CustomResponse;
import com.example.app.common.dto.request.enums.StatusFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/stations")
@RequiredArgsConstructor
public class StationViewController {

    private final StationViewService stationViewService;

    // get stations by activeType
    @GetMapping("")
    public ResponseEntity<CustomResponse<CustomPage<StationSummaryResponse>>> getStations(
            @RequestParam(defaultValue = "ACTIVE") StatusFilter status,
            @PageableDefault(size = 5, sort = "name", direction = Sort.Direction.ASC) Pageable pageable
    ) {
        return CustomResponse.ok(stationViewService.getStations(status, pageable));
    }

    @GetMapping("/{stationId}")
    public ResponseEntity<CustomResponse<StationDetailResponse>> getStation(
            @PathVariable Integer stationId
    ) {
        StationDetailResponse res = stationViewService.getStationById(stationId);
        return CustomResponse.ok(res);
    }
}
