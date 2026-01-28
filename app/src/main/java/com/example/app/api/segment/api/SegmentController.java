package com.example.app.api.segment.api;

import com.example.app.api.segment.api.dto.request.UpdateSegmentRequest;
import com.example.app.api.segment.application.SegmentService;
import com.example.app.common.response.CustomResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/segments")
@RequiredArgsConstructor
public class SegmentController {

    private final SegmentService segmentService;

    // patch station
    @PatchMapping("/{segmentId}")
    public ResponseEntity<CustomResponse<Void>> updateSegment(
            @PathVariable Integer segmentId,
            @Valid @RequestBody UpdateSegmentRequest request
    ) {
        segmentService.updateSegment(segmentId, request);
        return CustomResponse.ok();
    }


}
