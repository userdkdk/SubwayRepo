package com.example.app.api.station.api;

import com.example.app.api.station.api.dto.request.CreateStationRequest;
import com.example.app.api.station.api.dto.request.UpdateStationAttributeRequest;
import com.example.app.api.station.api.dto.request.UpdateStationStatusRequest;
import com.example.app.api.station.application.StationService;
import com.example.app.common.dto.response.CustomResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/stations")
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

    // update station attribute
    @PatchMapping("/{stationId}")
    public ResponseEntity<CustomResponse<Void>> updateStationAttribute(
            @PathVariable Integer stationId,
            @Valid @RequestBody UpdateStationAttributeRequest request
    ) {
        stationService.updateStationAttribute(stationId, request);
        return CustomResponse.ok();
    }

    // update station status
    @PostMapping("/{stationId}/activation")
    public ResponseEntity<CustomResponse<Void>> updateStationStatus(
            @PathVariable Integer stationId,
            @Valid @RequestBody UpdateStationStatusRequest request
    ) {
        stationService.updateStationStatus(stationId, request);
        return CustomResponse.ok();
    }
}
