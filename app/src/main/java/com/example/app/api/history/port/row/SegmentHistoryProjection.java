package com.example.app.api.history.port.row;

import com.example.core.domain.segmentHistory.HistoryType;

import java.time.LocalDateTime;

public record SegmentHistoryProjection(
        Integer id,
        Integer segmentId,
        HistoryType historyType,
        LocalDateTime changedAt
) {}
