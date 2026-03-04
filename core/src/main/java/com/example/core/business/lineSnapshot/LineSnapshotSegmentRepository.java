package com.example.core.business.lineSnapshot;

import java.util.List;

public interface LineSnapshotSegmentRepository {
    void insertAllByLineId(Integer snapshotId, Integer id);
}
