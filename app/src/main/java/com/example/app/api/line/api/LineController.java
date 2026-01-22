package com.example.app.api.line.api;

import com.example.app.api.line.api.dto.request.CreateLineRequest;
import com.example.app.api.line.api.dto.request.CreateSegmentRequest;
import com.example.app.api.line.api.dto.request.UpdateLineRequest;
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

    // create line
    @PostMapping("")
    public ResponseEntity<CustomResponse<Void>> createLine(
            @Valid @RequestBody CreateLineRequest request
    ) {
        lineService.createLine(request);
        return CustomResponse.created();
    }

    // patch line
    @PatchMapping("/{lineId}")
    public ResponseEntity<CustomResponse<Void>> updateLine(
            @PathVariable Integer lineId,
            @Valid @RequestBody UpdateLineRequest request
    ) {
        lineService.updateLine(lineId, request);
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
