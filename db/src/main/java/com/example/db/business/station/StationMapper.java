package com.example.db.business.station;

import com.example.core.domain.station.Station;
import com.example.core.domain.station.StationName;
import org.springframework.stereotype.Component;

@Component
public class StationMapper {
    public StationJpaEntity toNewEntity(Station station) {
        return StationJpaEntity.create(
                station.getName(),
                station.getActiveType());
    }

    public Station toDomain(StationJpaEntity stationEntity) {
        return Station.of(stationEntity.getId(),
                new StationName(stationEntity.getName()),
                stationEntity.getActiveType());
    }
}
