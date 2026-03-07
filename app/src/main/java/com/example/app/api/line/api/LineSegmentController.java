package com.example.app.api.line.api;

import com.example.app.api.line.api.dto.request.segment.CreateSegmentRequest;
import com.example.app.api.line.api.dto.request.segment.RemoveStationRequest;
import com.example.app.api.line.application.LineSegmentService;
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

    // add segment in line
    @PostMapping("/{lineId}/stations/{stationId}")
    public ResponseEntity<CustomResponse<Void>> addStation(
            @PathVariable Integer lineId,
            @PathVariable Integer stationId,
            @Valid @RequestBody CreateSegmentRequest request
    ) {
        lineSegmentService.addStation(lineId, stationId, request);
        return CustomResponse.created();
    }

    // remove station in line
    @DeleteMapping("/{lineId}/stations/{stationId}")
    public ResponseEntity<CustomResponse<Void>> removeStationInLine(
            @PathVariable Integer lineId,
            @PathVariable Integer stationId
    ) {
        lineSegmentService.removeStationInLine(lineId, stationId);
        return CustomResponse.ok();
    }
}
