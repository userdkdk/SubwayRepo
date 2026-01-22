package com.example.core.business.lineStation;

import com.example.core.common.domain.enums.ActiveType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class LineStation {
    private final Integer id;
    private final Integer lineId;
    private final Integer stationId;
    private ActiveType activeType;

    public static LineStation create(Integer lineId, Integer stationId) {
        return new LineStation(null, lineId, stationId, ActiveType.ACTIVE);
    }
}
