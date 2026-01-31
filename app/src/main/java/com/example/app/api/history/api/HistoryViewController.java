package com.example.app.api.history.api;

import com.example.app.api.history.api.dto.item.SegmentHistorySearchCondition;
import com.example.app.api.history.api.dto.response.SegmentHistoryResponse;
import com.example.app.api.history.application.HistoryViewService;
import com.example.app.common.response.CustomResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/histories")
@RequiredArgsConstructor
public class HistoryViewController {

    private final HistoryViewService historyViewService;

    @GetMapping()
    public ResponseEntity<CustomResponse<List<SegmentHistoryResponse>>> histories (
            @Valid @ParameterObject SegmentHistorySearchCondition cond
    ) {
        return CustomResponse.ok(historyViewService.search(null,cond));
    }

    @GetMapping("/{segmentId}")
    public ResponseEntity<CustomResponse<List<SegmentHistoryResponse>>> historiesBySeg (
            @PathVariable Integer segmentId,
            @Valid @ParameterObject SegmentHistorySearchCondition cond
    ) {
        return CustomResponse.ok(historyViewService.search(segmentId,cond));
    }
}
