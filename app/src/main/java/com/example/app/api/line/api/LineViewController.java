package com.example.app.api.line.api;

import com.example.app.api.line.application.LineViewService;
import com.example.app.api.station.api.dto.response.StationSegmentResponse;
import com.example.app.common.response.CustomResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/line")
@RequiredArgsConstructor
public class LineViewController {

    private final LineViewService lineViewService;

    @GetMapping("/{lineId}")
    public ResponseEntity<CustomResponse<List<StationSegmentResponse>>> getStationsById(
            @PathVariable Integer lineId
    ) {
        List<StationSegmentResponse> res = lineViewService.getStationsById(lineId);
        return CustomResponse.ok(res);
    }
}
