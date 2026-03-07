package com.example.db.business.lineSnapshot.snapthotSegment;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EqualsAndHashCode
public class LineSnapshotSegmentId implements Serializable {

    @Column(name = "snapshot_id", nullable = false)
    private Integer snapshotId;

    @Column(name = "segment_id", nullable = false)
    private Integer segmentId;
}
