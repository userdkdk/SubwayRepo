package com.example.app.api.history.api;

import com.example.app.api.history.api.dto.item.SegmentHistorySearchCondition;
import com.example.app.api.history.api.dto.response.SegmentHistoryResponse;
import com.example.app.api.history.application.HistoryViewService;
import com.example.app.common.dto.response.CustomPage;
import com.example.app.common.dto.response.CustomResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/histories")
@RequiredArgsConstructor
public class HistoryViewController {

    private final HistoryViewService historyViewService;

    @GetMapping()
    public ResponseEntity<CustomResponse<CustomPage<SegmentHistoryResponse>>> histories (
            @Valid @ParameterObject SegmentHistorySearchCondition cond,
            @PageableDefault(size = 5, sort = "changed_at", direction = Sort.Direction.DESC) Pageable pageable
            ) {
        return CustomResponse.ok(historyViewService.search(null,cond, pageable));
    }

    @GetMapping("/{segmentId}")
    public ResponseEntity<CustomResponse<CustomPage<SegmentHistoryResponse>>> historiesBySeg (
            @PathVariable Integer segmentId,
            @Valid @ParameterObject SegmentHistorySearchCondition cond,
            @PageableDefault(size = 2, sort = "changed_at", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return CustomResponse.ok(historyViewService.search(segmentId,cond, pageable));
    }
}
