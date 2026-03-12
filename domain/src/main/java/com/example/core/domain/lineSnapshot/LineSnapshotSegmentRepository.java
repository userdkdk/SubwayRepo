package com.example.core.domain.lineSnapshot;

import java.util.List;

public interface LineSnapshotSegmentRepository {
    int insertAll(Integer snapshotId, List<Integer> segmentIds);
}
