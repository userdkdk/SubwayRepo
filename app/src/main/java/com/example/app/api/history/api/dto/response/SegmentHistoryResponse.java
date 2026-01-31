package com.example.app.api.history.api.dto.response;

import com.example.app.business.segmentHistory.projection.SegmentHistoryProjection;
import com.example.core.business.segmentHistory.HistoryType;

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
