package com.example.db.business.lineSnapshot;

import com.example.core.business.lineSnapshot.LineSnapshot;
import com.example.core.business.lineSnapshot.LineSnapshotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class LineSnapshotRepositoryAdapter implements LineSnapshotRepository {
    private final SpringDataLineSnapshotJpaRepository snapshotRepository;
    private final SpringDataLineSnapshotSegmentJpaRepository snapshotSegmentRepository;
    private final LineSnapshotMapper snapshotMapper;

    @Override
    public Integer save(LineSnapshot lineSnapshot) {
        LineSnapshotJpaEntity entity = snapshotRepository.save(snapshotMapper.toNewEntity(lineSnapshot));
        return entity.getId();
    }

    @Override
    public List<Integer> findSegsIdByLine(Integer id) {
        return snapshotRepository.findSegsIdByLine(id);
    }
}
