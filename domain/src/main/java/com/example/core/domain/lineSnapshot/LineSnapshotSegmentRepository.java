package com.example.core.domain.lineSnapshot;

public interface LineSnapshotSegmentRepository {
    int insertAllByLineId(Integer snapshotId, Integer lineId);
}
