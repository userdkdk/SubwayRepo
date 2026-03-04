package com.example.db.business.lineSnapshot;

import com.example.core.business.lineSnapshot.LineSnapshotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class LineSnapshotRepositoryAdapter implements LineSnapshotRepository {
    private final SpringDataLineSnapshotJpaRepository snapshotRepository;
    private final SpringDataLineSnapshotSegmentJpaRepository snapshotSegmentRepository;

    @Override
    public Integer save(Integer id) {
        return 0;
    }

    @Override
    public List<Integer> findSegsIdByLine(Integer id) {
        return snapshotRepository.findSegsIdByLine(id);
    }
}
