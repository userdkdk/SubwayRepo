package com.example.app.business.segment.segmentHistory;

import com.example.core.business.segmentHistory.SegmentHistory;
import org.springframework.stereotype.Component;

@Component
public class SegmentHistoryMapper {
    public SegmentHistoryJpaEntity toEntity(SegmentHistory segmentHistory) {
        return SegmentHistoryJpaEntity.from(
                segmentHistory.getSegmentId(),
                segmentHistory.getHistoryType()
        );
    }
}
