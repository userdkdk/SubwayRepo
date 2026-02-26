package com.example.app.api.line.api;

import com.example.app.api.line.api.dto.request.line.CreateLineRequest;
import com.example.app.api.line.api.dto.request.line.UpdateLineAttributeRequest;
import com.example.app.api.line.api.dto.request.line.ActivateLineRequest;
import com.example.app.api.line.application.LineService;
import com.example.app.common.dto.response.CustomResponse;
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

    // activate line
    @PostMapping("/{lineId}/activate")
    public ResponseEntity<CustomResponse<Void>> activateLine(
            @PathVariable Integer lineId,
            @Valid @RequestBody ActivateLineRequest request
    ) {
        lineService.activateLine(lineId, request);
        return CustomResponse.ok();
    }

    // deactivate line
    @PostMapping("/{lineId}/deactivate")
    public ResponseEntity<CustomResponse<Void>> deactivateLine(
            @PathVariable Integer lineId
    ) {
        lineService.deactivateLine(lineId);
        return CustomResponse.ok();
    }
}
