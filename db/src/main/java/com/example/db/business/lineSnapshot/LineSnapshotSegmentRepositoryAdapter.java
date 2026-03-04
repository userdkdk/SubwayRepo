package com.example.db.business.lineSnapshot;

import com.example.core.business.lineSnapshot.LineSnapshotSegmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class LineSnapshotSegmentRepositoryAdapter implements LineSnapshotSegmentRepository {
    private final SpringDataLineSnapshotSegmentJpaRepository snapshotSegmentRepository;

    @Override
    public void insertAllByLineId(Integer snapshotId, Integer id) {
    }

}
