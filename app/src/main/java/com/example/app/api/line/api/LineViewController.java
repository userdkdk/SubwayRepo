package com.example.app.api.line.api;

import com.example.app.api.line.api.dto.response.LineDetailResponse;
import com.example.app.api.line.api.dto.response.LineResponse;
import com.example.app.api.line.application.LineViewService;
import com.example.app.common.dto.request.ViewRequestFilter;
import com.example.app.common.dto.response.CustomPage;
import com.example.app.common.dto.response.CustomResponse;
import com.example.db.common.domain.enums.StatusFilter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/lines")
@RequiredArgsConstructor
public class LineViewController {

    private final LineViewService lineViewService;

    // get lines by filter
    @GetMapping("")
    public ResponseEntity<CustomResponse<CustomPage<LineResponse>>> getAllLines(
            @ModelAttribute @Valid ViewRequestFilter request
    ) {
        CustomPage<LineResponse> res = lineViewService.getLines(request);
        return CustomResponse.ok(res);
    }

    @GetMapping("/{lineId}")
    public ResponseEntity<CustomResponse<LineDetailResponse>> getStationsById(
            @PathVariable Integer lineId,
            @RequestParam(defaultValue = "ACTIVE") StatusFilter status
    ) {
        LineDetailResponse res = lineViewService.getStationsById(lineId, status);
        return CustomResponse.ok(res);
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<CustomResponse<LineDetailResponse>> getStationsByName(
            @PathVariable Integer lineId,
            @RequestParam(defaultValue = "ACTIVE") StatusFilter status
    ) {
        LineDetailResponse res = lineViewService.getStationsById(lineId, status);
        return CustomResponse.ok(res);
    }
}
