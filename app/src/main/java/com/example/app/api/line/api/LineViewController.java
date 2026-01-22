package com.example.app.api.line.api;

import com.example.app.api.line.api.dto.response.LineResponse;
import com.example.app.api.line.application.LineViewService;
import com.example.app.api.station.api.dto.response.StationSegmentResponse;
import com.example.app.common.response.CustomResponse;
import com.example.app.common.response.enums.StatusFilter;
import com.example.core.common.domain.enums.ActiveType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/line")
@RequiredArgsConstructor
public class LineViewController {

    private final LineViewService lineViewService;

    // get lines by activeType
    @GetMapping("")
    public ResponseEntity<CustomResponse<List<LineResponse>>> getAllLines(
            @RequestParam(defaultValue = "ACTIVE") StatusFilter status
    ) {
        List<LineResponse> res = lineViewService.getLines(status);
        return CustomResponse.ok(res);
    }

    @GetMapping("/{lineId}")
    public ResponseEntity<CustomResponse<List<StationSegmentResponse>>> getStationsById(
            @PathVariable Integer lineId,
            @RequestParam(defaultValue = "ACTIVE") StatusFilter status
    ) {
        List<StationSegmentResponse> res = lineViewService.getStationsById(lineId, status);
        return CustomResponse.ok(res);
    }
}
