package com.example.core.business.segmentHistory;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class SegmentHistory {
    private final Integer id;
    private final Integer segmentId;
    private final HistoryType historyType;

    public static SegmentHistory create(Integer segmentId) {
        return new SegmentHistory(null, segmentId, HistoryType.CREATE);
    }

    public static SegmentHistory update(Integer segmentId) {
        return new SegmentHistory(null, segmentId, HistoryType.UPDATE);
    }
}
