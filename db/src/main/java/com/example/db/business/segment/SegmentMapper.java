package com.example.db.business.segment;

import com.example.db.business.line.LineJpaEntity;
import com.example.db.business.station.StationJpaEntity;
import com.example.core.domain.segment.Segment;
import com.example.core.domain.segment.SegmentAttribute;
import org.springframework.stereotype.Component;

@Component
public class SegmentMapper {
    public SegmentJpaEntity toNewEntity(Segment segment, LineJpaEntity lineRef, StationJpaEntity beforeRef, StationJpaEntity afterRef) {
        return SegmentJpaEntity.create(
                lineRef,
                beforeRef,
                afterRef,
                segment.getSegmentAttribute().distance(),
                segment.getSegmentAttribute().spendTimeSeconds()
        );
    }
    public Segment toDomain(SegmentJpaEntity saved) {
        SegmentAttribute segmentAttribute = new SegmentAttribute(saved.getDistance(), saved.getSpendTime());
        return Segment.of(saved.getId(),
                saved.getLineJpaEntity().getId(),
                saved.getBeforeStationJpaEntity().getId(),
                saved.getAfterStationJpaEntity().getId(),
                segmentAttribute,
                saved.getActiveType());
    }
}
