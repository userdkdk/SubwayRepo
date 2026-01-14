package com.example.app.business.station.adapter;

import com.example.core.business.station.Station;
import com.example.core.business.station.StationRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class StationRepositoryAdapter implements StationRepository {
    private final SpringDataStationJpaRepository springDataStationJpaRepository;


    @Override
    public boolean existsByName(String name) {
        return false;
    }

    @Override
    public Station save(Station station) {
        return null;
    }
}
