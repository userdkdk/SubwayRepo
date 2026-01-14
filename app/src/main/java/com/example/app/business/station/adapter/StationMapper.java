package com.example.app.business.station.adapter;

import com.example.core.business.station.Station;
import org.springframework.stereotype.Component;

@Component
public class StationMapper {
    public StationJpaEntity toEntity(Station station) {
        return StationJpaEntity.create(station.getName(), station.getActiveType());
    }
}
