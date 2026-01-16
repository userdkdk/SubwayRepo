package com.example.app.business.line.api;

import com.example.app.business.line.api.dto.CreateLineRequest;
import com.example.app.business.line.application.LineService;
import com.example.app.common.response.CustomResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/line")
@RequiredArgsConstructor
public class LineController {

    private final LineService lineService;

    @PostMapping("")
    public ResponseEntity<CustomResponse<Void>> createStation(
            @RequestBody CreateLineRequest request
    ) {
        lineService.createLine(request);
        return CustomResponse.created();
    }

}
