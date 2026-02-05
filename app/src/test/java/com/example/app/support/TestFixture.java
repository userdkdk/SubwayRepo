package com.example.app.support;

import com.example.app.business.line.LineJpaEntity;
import com.example.app.business.segment.SegmentJpaEntity;
import com.example.app.business.station.StationJpaEntity;
import com.example.core.common.domain.enums.ActiveType;

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
            Double distance, Integer spendTime) {
        return SegmentJpaEntity.create(line, s1, s2, distance, spendTime);
    }
}
