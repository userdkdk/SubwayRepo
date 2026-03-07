package com.example.app.api.history.api.dto.response;

import com.example.db.business.segmentHistory.SegmentHistoryProjection;
import com.example.core.domain.segmentHistory.HistoryType;

import java.time.LocalDateTime;

public record SegmentHistoryResponse(
        Integer id,
        Integer segmentId,
        HistoryType historyType,
        LocalDateTime changedAt
) {
    public static SegmentHistoryResponse from(SegmentHistoryProjection p) {
        return new SegmentHistoryResponse(
                p.id(),
                p.segmentId(),
                p.historyType(),
                p.changedAt()
        );
    }
}
