package com.example.app.api.line.api;

import com.example.app.api.line.api.dto.request.CreateLineRequest;
import com.example.app.api.line.api.dto.request.CreateSegmentRequest;
import com.example.app.api.line.api.dto.request.UpdateSegmentRequest;
import com.example.app.api.line.application.LineService;
import com.example.app.common.response.CustomResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/line")
@RequiredArgsConstructor
public class LineController {

    private final LineService lineService;

    @PostMapping("")
    public ResponseEntity<CustomResponse<Void>> createLine(
            @Valid @RequestBody CreateLineRequest request
    ) {
        lineService.createLine(request);
        return CustomResponse.created();
    }

    @PostMapping("/{lineId}/stations")
    public ResponseEntity<CustomResponse<Void>> addStation(
            @PathVariable Integer lineId,
            @Valid @RequestBody CreateSegmentRequest request
    ) {
        lineService.addStation(lineId, request);
        return CustomResponse.created();
    }

    @PutMapping("/{lineId}/stations")
    public ResponseEntity<CustomResponse<Void>> inActiveStation(
            @PathVariable Integer lineId,
            @Valid @RequestBody UpdateSegmentRequest request
    ) {
        lineService.inActiveStation(lineId, request);
        return CustomResponse.ok();
    }

}
