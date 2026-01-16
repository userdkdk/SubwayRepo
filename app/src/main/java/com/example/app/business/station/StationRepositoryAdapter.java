package com.example.app.business.station;

import com.example.core.business.station.Station;
import com.example.core.business.station.StationRepository;
import com.example.core.common.domain.enums.ActiveType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class StationRepositoryAdapter implements StationRepository {
    private final SpringDataStationJpaRepository stationJpaRepository;
    private final StationMapper stationMapper;

    @Override
    public Optional<Station> findByName(String name) {
        return stationJpaRepository.findByName(name).map(stationMapper::toDomain);
    }

    @Override
    public Optional<Integer> findIdByName(String name) {
        return stationJpaRepository.findIdByName(name);
    }


    @Override
    public Station upsertActivateByName(String rawName) {
        Station station = Station.create(rawName);
        String name = station.getName();

        int isUpdated = stationJpaRepository.setActivateByName(name, ActiveType.ACTIVE);

        if (isUpdated==0) {
            StationJpaEntity saved = stationJpaRepository.save(
                    StationJpaEntity.create(name, ActiveType.ACTIVE)
            );
            return stationMapper.toDomain(saved);
        }
        return station;
    }

    @Override
    public void inActivateByName(String rawName) {
        Station station = Station.create(rawName);
        String name = station.getName();
        stationJpaRepository.setActivateByName(name, ActiveType.INACTIVE);
    }
}
