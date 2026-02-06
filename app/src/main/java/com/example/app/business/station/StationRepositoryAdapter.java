package com.example.app.business.station;

import com.example.app.common.exception.AppErrorCode;
import com.example.core.business.station.Station;
import com.example.core.business.station.StationRepository;
import com.example.core.common.domain.enums.ActiveType;
import com.example.core.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@Component
@RequiredArgsConstructor
public class StationRepositoryAdapter implements StationRepository {
    private final SpringDataStationJpaRepository stationJpaRepository;
    private final StationMapper stationMapper;

    @Override
    public void save(Station station) {
        stationJpaRepository.save(
                stationMapper.toNewEntity(station)
        );
    }

    @Override
    public void update(Integer id, Consumer<Station> updater) {
        // get station
        StationJpaEntity entity = stationJpaRepository.findById(id)
                .orElseThrow(()->CustomException.app(AppErrorCode.STATION_NOT_FOUND)
                        .addParam("id", id));
        Station domain = stationMapper.toDomain(entity);
        updater.accept(domain);

        entity.setName(domain.getName().value());
        entity.setActiveType(domain.getActiveType());
    }

    @Override
    public void ensureNameUnique(String name) {
        if (stationJpaRepository.existsByName(name)) {
            throw CustomException.app(AppErrorCode.STATION_NAME_DUPLICATED)
                    .addParam("name", name);
        }
    }

    @Override
    public boolean existsActiveById(Integer id) {
        return stationJpaRepository.existsByIdAndActiveType(id, ActiveType.ACTIVE);
    }
}
