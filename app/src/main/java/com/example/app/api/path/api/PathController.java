package com.example.app.api.path.api;

import com.example.app.api.path.api.dto.response.PathResponse;
import com.example.app.api.path.api.enums.PathFilter;
import com.example.app.api.path.application.PathService;
import com.example.app.common.response.CustomResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/path")
@RequiredArgsConstructor
public class PathController {

    private final PathService pathService;

    @GetMapping("/{lineId}/{startId}/{endId}")
    public ResponseEntity<CustomResponse<PathResponse>> getPath(
            @PathVariable Integer startId,
            @PathVariable Integer endId,
            @RequestParam(defaultValue = "TIME") PathFilter pathFilter
            ) {
        PathResponse res = pathService.getPath(startId, endId, pathFilter);
        return CustomResponse.ok(res);
    }
}
