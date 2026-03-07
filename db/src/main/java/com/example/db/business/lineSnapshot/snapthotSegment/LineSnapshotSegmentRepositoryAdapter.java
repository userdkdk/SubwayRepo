package com.example.db.business.lineSnapshot.snapthotSegment;

import com.example.core.domain.lineSnapshot.LineSnapshotSegmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LineSnapshotSegmentRepositoryAdapter implements LineSnapshotSegmentRepository {
    private final SpringDataLineSnapshotSegmentJpaRepository snapshotSegmentRepository;

    @Override
    public int insertAllByLineId(Integer snapshotId, Integer lineId) {
        return snapshotSegmentRepository.insertAllAcitvateByLineId(snapshotId, lineId);
    }

}
