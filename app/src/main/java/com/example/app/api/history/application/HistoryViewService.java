package com.example.app.api.history.application;

import com.example.app.api.history.api.dto.item.SegmentHistorySearchCondition;
import com.example.app.api.history.api.dto.response.SegmentHistoryResponse;
import com.example.app.business.segmentHistory.SegmentHistoryQueryRepository;
import com.example.app.business.segmentHistory.projection.SegmentHistoryProjection;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HistoryViewService {

    private final SegmentHistoryQueryRepository historyQueryRepository;

    public List<SegmentHistoryResponse> search(Integer segmentId, SegmentHistorySearchCondition cond) {
        List<SegmentHistoryProjection> rows = historyQueryRepository.findHistory(
                segmentId, cond.actions(), cond.from(), cond.to());

        return rows.stream()
                .map(SegmentHistoryResponse::from)
                .toList();
    }
}
