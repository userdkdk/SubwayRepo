package com.example.core.business.segment;

import com.example.core.common.domain.enums.ActiveType;
import com.example.core.exception.CustomException;
import com.example.core.exception.DomainErrorCode;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Segment {

    private final Integer id;
    private final Integer lineId;
    private final Integer beforeStationId;
    private final Integer afterStationId;
    private SegmentAttribute segmentAttribute;
    private ActiveType activeType;

    public static Segment create(Integer lineId, Integer beforeStationId, Integer afterStationId,
                                  SegmentAttribute segmentAttribute) {
        if (beforeStationId.equals(afterStationId)) {
            throw CustomException.domain(DomainErrorCode.SEGMENT_INPUT_STATION_SAME)
                    .addParam("station id", beforeStationId);
        }
        return new Segment(null, lineId, beforeStationId, afterStationId,
                segmentAttribute, ActiveType.ACTIVE);
    }

    public static Segment of(Integer id, Integer lineId, Integer beforeStationId,
                             Integer afterStationId, SegmentAttribute segmentAttribute, ActiveType activeType) {
        return new Segment(id, lineId, beforeStationId, afterStationId, segmentAttribute, activeType);
    }

    public void changeSegmentAttribute(SegmentAttribute segmentAttribute) {
        this.segmentAttribute = segmentAttribute;
    }
}
