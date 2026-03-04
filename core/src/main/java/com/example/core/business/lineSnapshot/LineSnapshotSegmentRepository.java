package com.example.core.business.lineSnapshot;

import java.util.List;

public interface LineSnapshotSegmentRepository {
    int insertAllByLineId(Integer snapshotId, Integer lineId);
}
