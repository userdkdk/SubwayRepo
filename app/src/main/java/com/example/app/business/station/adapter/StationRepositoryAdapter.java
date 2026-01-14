package com.example.app.business.station.adapter;

import com.example.core.business.station.Station;
import com.example.core.business.station.StationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StationRepositoryAdapter implements StationRepository {
    private final SpringDataStationJpaRepository stationJpaRepository;
    private final StationMapper stationMapper;

    @Override
    public boolean existsByName(String name) {
        return stationJpaRepository.existsByName(name);
    }

    @Override
    public void save(Station station) {
        stationJpaRepository.save(stationMapper.toEntity(station));
    }
}
