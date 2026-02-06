package com.example.app.api.line.api;

import com.example.app.api.line.api.dto.request.CreateLineRequest;
import com.example.app.api.line.api.dto.request.CreateSegmentRequest;
import com.example.app.api.line.api.dto.request.UpdateLineAttributeRequest;
import com.example.app.api.line.api.dto.request.UpdateLineStatusRequest;
import com.example.app.api.line.application.LineService;
import com.example.app.common.response.CustomResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/lines")
@RequiredArgsConstructor
public class LineController {

    private final LineService lineService;

    // create line
    @PostMapping("")
    public ResponseEntity<CustomResponse<Void>> createLine(
            @Valid @RequestBody CreateLineRequest request
    ) {
        lineService.createLine(request);
        return CustomResponse.created();
    }

    // update line attribute
    @PatchMapping("/{lineId}")
    public ResponseEntity<CustomResponse<Void>> updateLineAttribute(
            @PathVariable Integer lineId,
            @Valid @RequestBody UpdateLineAttributeRequest request
    ) {
        lineService.updateLineAttribute(lineId, request);
        return CustomResponse.ok();
    }

    // update line status
    @PostMapping("/{lineId}/activation")
    public ResponseEntity<CustomResponse<Void>> updateLine(
            @PathVariable Integer lineId,
            @Valid @RequestBody UpdateLineStatusRequest request
    ) {
        lineService.updateLineStatus(lineId, request);
        return CustomResponse.ok();
    }

    // create segment
    @PostMapping("/{lineId}/stations")
    public ResponseEntity<CustomResponse<Void>> addStation(
            @PathVariable Integer lineId,
            @Valid @RequestBody CreateSegmentRequest request
    ) {
        lineService.addStation(lineId, request);
        return CustomResponse.created();
    }

}
