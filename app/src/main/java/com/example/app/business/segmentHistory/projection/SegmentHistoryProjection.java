package com.example.app.business.segmentHistory.projection;

import com.example.core.business.segmentHistory.HistoryType;
import com.querydsl.core.annotations.QueryProjection;

import java.time.LocalDateTime;

public record SegmentHistoryProjection(
        Integer id,
        Integer segmentId,
        HistoryType historyType,
        LocalDateTime changedAt
) {
    @QueryProjection
    public SegmentHistoryProjection {}
}
