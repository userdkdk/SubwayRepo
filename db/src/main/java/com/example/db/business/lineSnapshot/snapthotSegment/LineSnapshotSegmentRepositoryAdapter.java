package com.example.db.business.lineSnapshot.snapthotSegment;

import com.example.core.domain.lineSnapshot.LineSnapshotSegmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class LineSnapshotSegmentRepositoryAdapter implements LineSnapshotSegmentRepository {
    private final SpringDataLineSnapshotSegmentJpaRepository snapshotSegmentRepository;

    @Override
    public int insertAll(Integer snapshotId, List<Integer> segmentIds) {
        return snapshotSegmentRepository.insertAll(snapshotId, segmentIds);
    }

}
