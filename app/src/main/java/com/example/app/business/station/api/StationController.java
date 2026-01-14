package com.example.app.business.station.api;

import com.example.app.business.station.api.dto.CreateStationRequest;
import com.example.app.business.station.application.StationService;
import com.example.app.common.response.CustomResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/station")
@RequiredArgsConstructor
public class StationController {

    private final StationService stationService;

    @PostMapping("")
    public ResponseEntity<CustomResponse<Void>> createStation(
            @RequestBody CreateStationRequest request
            ) {
        stationService.createStation(request);
        return CustomResponse.created();
    }

}
