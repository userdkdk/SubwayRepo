package com.example.db.business.lineSnapshot.snapthotSegment;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "line_snapshots_segments")
public class LineSnapshotSegmentJpaEntity {

    @EmbeddedId
    private LineSnapshotSegmentId id;

    public static LineSnapshotSegmentJpaEntity create(Integer snapshotId, Integer segmentId) {
        LineSnapshotSegmentJpaEntity e = new LineSnapshotSegmentJpaEntity();
        e.id = new LineSnapshotSegmentId(snapshotId, segmentId);
        return e;
    }
}
