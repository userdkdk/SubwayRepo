package com.example.app.business.station;

import com.example.core.business.station.Station;
import com.example.core.business.station.StationRepository;
import com.example.core.common.domain.enums.ActiveType;
import com.example.core.common.exception.CustomException;
import com.example.core.common.exception.DomainErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.function.Consumer;

@Component
@RequiredArgsConstructor
public class StationRepositoryAdapter implements StationRepository {
    private final SpringDataStationJpaRepository stationJpaRepository;
    private final StationMapper stationMapper;

    @Override
    public Station save(Station station) {
        String name = station.getName();

        if (stationJpaRepository.existsByName(name)) {
            throw CustomException.domain(DomainErrorCode.STATION_NAME_DUPLICATED)
                    .addParam("name", name);
        }

        StationJpaEntity saved = stationJpaRepository.save(
                stationMapper.toNewEntity(station)
        );
        return stationMapper.toDomain(saved);
    }

    @Override
    public void update(Integer id, Consumer<Station> updater) {

    }
}
