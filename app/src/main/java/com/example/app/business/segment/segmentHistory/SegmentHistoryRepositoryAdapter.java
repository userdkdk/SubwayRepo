package com.example.app.business.segment.segmentHistory;

import com.example.core.business.segmentHistory.SegmentHistory;
import com.example.core.business.segmentHistory.SegmentHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SegmentHistoryRepositoryAdapter implements SegmentHistoryRepository {
    private final SpringDataSegmentHistoryJpaRepository historyJpaRepository;
    private final SegmentHistoryMapper historyMapper;

    @Override
    public void save(SegmentHistory segmentHistory) {
        historyJpaRepository.save(historyMapper.toEntity(segmentHistory));
    }
}
