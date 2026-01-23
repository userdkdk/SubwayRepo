package com.example.app.api.station.api;

import com.example.app.api.station.api.dto.request.CreateStationRequest;
import com.example.app.api.station.api.dto.request.UpdateStationRequest;
import com.example.app.api.station.application.StationService;
import com.example.app.common.response.CustomResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/station")
@RequiredArgsConstructor
public class StationController {

    private final StationService stationService;

    // create station
    @PostMapping("")
    public ResponseEntity<CustomResponse<Void>> createStation(
            @Valid @RequestBody CreateStationRequest request
            ) {
        stationService.createStation(request);
        return CustomResponse.created();
    }

    // patch station
    @PatchMapping("/{stationId}")
    public ResponseEntity<CustomResponse<Void>> updateStationActivate(
            @PathVariable Integer stationId,
            @Valid @RequestBody UpdateStationRequest request
    ) {
        stationService.updateStation(stationId, request);
        return CustomResponse.created();
    }

}
