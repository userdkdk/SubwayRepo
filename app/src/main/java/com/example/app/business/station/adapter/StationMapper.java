package com.example.app.business.station.adapter;

import com.example.core.business.station.Station;
import org.springframework.stereotype.Component;

@Component
public class StationMapper {
    public StationJpaEntity toNewEntity(Station station) {
        return StationJpaEntity.create(station.getName(), station.getActiveType());
    }

    public Station toDomain(StationJpaEntity stationEntity) {
        return Station.of(stationEntity.getId(), stationEntity.getName(), stationEntity.getActiveType());
    }
}
