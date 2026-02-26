package com.example.app.api.line.api;

import com.example.app.api.line.api.dto.request.segment.CreateSegmentRequest;
import com.example.app.api.line.api.dto.request.segment.RemoveStationRequest;
import com.example.app.api.line.api.dto.request.segment.RestoreStationRequest;
import com.example.app.api.line.application.LineSegmentService;
import com.example.app.api.station.api.dto.request.UpdateStationStatusRequest;
import com.example.app.common.dto.response.CustomResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/lines")
@RequiredArgsConstructor
public class LineSegmentController {
    private final LineSegmentService lineSegmentService;

    // create segment in line
    @PostMapping("/{lineId}/stations")
    public ResponseEntity<CustomResponse<Void>> addStation(
            @PathVariable Integer lineId,
            @Valid @RequestBody CreateSegmentRequest request
    ) {
        lineSegmentService.addStation(lineId, request);
        return CustomResponse.created();
    }

    // change station status in line
    @PatchMapping("/{lineId}/stations/{stationId}")
    public ResponseEntity<CustomResponse<Void>> updateStationStatusInLine(
            @PathVariable Integer lineId,
            @PathVariable Integer stationId,
            @Valid @RequestBody UpdateStationStatusRequest request
    ) {
        lineSegmentService.updateStationStatusInLine(lineId, stationId, request);
        return CustomResponse.ok();
    }

    // remove station in line
    @DeleteMapping("/{lineId}/stations/{stationId}")
    public ResponseEntity<CustomResponse<Void>> removeStationInLine(
            @PathVariable Integer lineId,
            @PathVariable Integer stationId,
            @Valid @RequestBody RemoveStationRequest request
    ) {
        lineSegmentService.removeStationInLine(lineId, stationId, request);
        return CustomResponse.ok();
    }

    // restore station in line
    @PostMapping("/{lineId}/stations/{stationId}/restore")
    public ResponseEntity<CustomResponse<Void>> restoreStationInLine(
            @PathVariable Integer lineId,
            @PathVariable Integer stationId,
            @Valid @RequestBody RestoreStationRequest request
    ) {
        lineSegmentService.restoreStationInLine(lineId, stationId, request);
        return CustomResponse.ok();
    }
}
