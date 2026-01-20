package com.example.app.business.segment;

import com.example.app.business.line.LineJpaEntity;
import com.example.app.business.station.StationJpaEntity;
import com.example.core.business.segment.Segment;
import org.springframework.stereotype.Component;

@Component
public class SegmentMapper {
    public SegmentJpaEntity toNewEntity(Segment segment, LineJpaEntity lineRef, StationJpaEntity beforeRef, StationJpaEntity afterRef) {
        return SegmentJpaEntity.create(
                lineRef,
                beforeRef,
                afterRef,
                segment.getDistance(),
                segment.getSpendTime()
        );
    }
}
