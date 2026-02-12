package com.example.app.api.history.application;

import com.example.app.api.history.api.dto.item.SegmentHistorySearchCondition;
import com.example.app.api.history.api.dto.response.SegmentHistoryResponse;
import com.example.app.business.segmentHistory.SegmentHistoryQueryRepository;
import com.example.app.business.segmentHistory.projection.SegmentHistoryProjection;
import com.example.app.common.dto.response.CustomPage;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HistoryViewService {

    private final SegmentHistoryQueryRepository historyQueryRepository;

    public CustomPage<SegmentHistoryResponse> search(Integer segmentId,
                                                     SegmentHistorySearchCondition cond,
                                                     Pageable pageable) {
        Page<SegmentHistoryProjection> rows = historyQueryRepository.findHistory(
                segmentId, cond.actions(), cond.from(), cond.to(), pageable);

        return new CustomPage<>(rows.map(SegmentHistoryResponse::from));
    }
}
