package com.example.app.api.line.api;

import com.example.app.api.line.api.dto.request.CreateLineRequest;
import com.example.app.api.line.application.LineService;
import com.example.app.api.station.api.dto.response.StationResponse;
import com.example.app.common.response.CustomResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/line")
@RequiredArgsConstructor
public class LineController {

    private final LineService lineService;

    @GetMapping("/{lineId}")
    public ResponseEntity<CustomResponse<List<StationResponse>>> getStationsById(
            @RequestParam Integer lineId
    ) {
        List<StationResponse> res = lineService.getStationsById(lineId);
        return CustomResponse.ok(res);
    }

    @PostMapping("")
    public ResponseEntity<CustomResponse<Void>> createLine(
            @RequestBody CreateLineRequest request
    ) {
        lineService.createLine(request);
        return CustomResponse.created();
    }

}
