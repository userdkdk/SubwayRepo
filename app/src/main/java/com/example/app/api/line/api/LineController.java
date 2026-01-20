package com.example.app.api.line.api;

import com.example.app.api.line.api.dto.request.CreateLineRequest;
import com.example.app.api.line.api.dto.request.CreateSegmentRequest;
import com.example.app.api.line.application.LineService;
import com.example.app.common.response.CustomResponse;
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
            @RequestBody CreateLineRequest request
    ) {
        lineService.createLine(request);
        return CustomResponse.created();
    }

    @PostMapping("/{lineId}")
    public ResponseEntity<CustomResponse<Void>> createSegment(
            @RequestParam Integer lineId,
            @RequestBody CreateSegmentRequest request
    ) {
        lineService.createSegment(lineId, request);
        return CustomResponse.created();
    }

}
