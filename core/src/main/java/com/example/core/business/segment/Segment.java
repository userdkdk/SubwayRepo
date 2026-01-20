package com.example.core.business.segment;

import com.example.core.common.domain.enums.ActiveType;
import com.example.core.common.exception.CustomException;
import com.example.core.common.exception.DomainErrorCode;
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
    private final double distance;
    private final int spendTime;
    private final ActiveType activeType;

    public static Segment create(Integer lineId, Integer beforeStationId, Integer afterStationId,
                                  double distance, int spendTime) {
        String segmentAttributes = "line id: "+ lineId + ", before station: "+beforeStationId+
                ", after station id"+afterStationId;
        if (beforeStationId.equals(afterStationId)) {
            throw CustomException.domain(DomainErrorCode.SEGMENT_INPUT_STATION_SAME)
                    .addParam("segment attribute", segmentAttributes);
        }
        if (distance<=0 || spendTime<=0) {
            throw CustomException.domain(DomainErrorCode.SEGMENT_INPUT_VALUE_ERROR)
                    .addParam("segment attribute", segmentAttributes)
                    .addParam("distance", distance)
                    .addParam("spend time", spendTime);
        }
        return new Segment(null, lineId, beforeStationId, afterStationId,
                distance, spendTime, ActiveType.ACTIVE);
    }

}
