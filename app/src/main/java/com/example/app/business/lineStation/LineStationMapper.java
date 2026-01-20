package com.example.app.business.lineStation;

import com.example.app.business.line.LineJpaEntity;
import com.example.app.business.station.StationJpaEntity;
import com.example.core.business.lineStation.LineStation;
import org.springframework.stereotype.Component;

@Component
public class LineStationMapper {
    public LineStationJpaEntity toNewEntity(LineJpaEntity lineRef, StationJpaEntity stationRef) {
        return LineStationJpaEntity.create(lineRef, stationRef);
    }

    public LineStation toDomain(LineStationJpaEntity entity) {
        return null;
    }
}
