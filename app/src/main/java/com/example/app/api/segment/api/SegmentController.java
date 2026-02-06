package com.example.app.api.segment.api;

import com.example.app.api.segment.api.dto.request.UpdateSegmentAttributeRequest;
import com.example.app.api.segment.api.dto.request.UpdateSegmentStatusRequest;
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

    // update segment attribute
    @PatchMapping("/{segmentId}")
    public ResponseEntity<CustomResponse<Void>> updateSegmentAttribute(
            @PathVariable Integer segmentId,
            @Valid @RequestBody UpdateSegmentAttributeRequest request
    ) {
        segmentService.updateSegmentAttribute(segmentId, request);
        return CustomResponse.ok();
    }

    // update segment status
    @PostMapping("/{segmentId}/activation")
    public ResponseEntity<CustomResponse<Void>> updateSegmentStatus(
            @PathVariable Integer segmentId,
            @Valid @RequestBody UpdateSegmentStatusRequest request
    ) {
        segmentService.updateSegmentStatus(segmentId, request);
        return CustomResponse.ok();
    }


}
