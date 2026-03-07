package com.example.db.business.segmentHistory;

import com.example.core.domain.segmentHistory.SegmentHistory;
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
