package com.example.core.domain.lineSnapshot;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class LineSnapshotSegment {
    private final Integer lineSnapshotId;
    private final Integer segmentId;
}
