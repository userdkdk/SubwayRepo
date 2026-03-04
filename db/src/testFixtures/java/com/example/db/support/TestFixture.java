package com.example.db.support;

import com.example.core.common.domain.enums.ActiveType;
import com.example.db.business.line.LineJpaEntity;
import com.example.db.business.lineSnapshot.LineSnapshotJpaEntity;
import com.example.db.business.lineSnapshot.LineSnapshotSegmentJpaEntity;
import com.example.db.business.segment.SegmentJpaEntity;
import com.example.db.business.station.StationJpaEntity;

public class TestFixture {
    public static StationJpaEntity station(String name) {
        return StationJpaEntity.create(name, ActiveType.ACTIVE);
    }

    public static LineJpaEntity line(String name) {
        return LineJpaEntity.create(name, ActiveType.ACTIVE);
    }

    public static SegmentJpaEntity segment(
            LineJpaEntity line,
            StationJpaEntity s1, StationJpaEntity s2,
            Double distance, Integer spendTime, ActiveType activeType) {
        SegmentJpaEntity entity = SegmentJpaEntity.create(line, s1, s2, distance, spendTime);
        entity.changeActiveType(activeType);
        return entity;
    }

    public static LineSnapshotJpaEntity snapshot(Integer lineId) {
        return LineSnapshotJpaEntity.create(lineId);
    }

    public static LineSnapshotSegmentJpaEntity snapshotSegment(Integer snapshotId, Integer segmentId) {
        return LineSnapshotSegmentJpaEntity.create(snapshotId, segmentId);
    }
}
