package com.example.app.business.station;

import com.example.app.common.exception.AppErrorCode;
import com.example.core.business.station.Station;
import com.example.core.business.station.StationRepository;
import com.example.core.common.domain.enums.ActiveType;
import com.example.core.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@Component
@RequiredArgsConstructor
public class StationRepositoryAdapter implements StationRepository {
    private final SpringDataStationJpaRepository stationJpaRepository;
    private final StationMapper stationMapper;

    @Override
    public void save(Station station) {
        try {
            stationJpaRepository.save(stationMapper.toNewEntity(station));
        } catch (DataIntegrityViolationException e) {
            throw CustomException.app(AppErrorCode.STATION_NAME_DUPLICATED)
                    .addParam("name", station.getName());
        }
    }

    @Override
    public void update(Integer id, Consumer<Station> updater) {
        // get station
        StationJpaEntity entity = stationJpaRepository.findById(id)
                .orElseThrow(()->CustomException.app(AppErrorCode.STATION_NOT_FOUND)
                        .addParam("id", id));
        Station domain = stationMapper.toDomain(entity);
        updater.accept(domain);

        entity.changeName(domain.getName());
        entity.changeActiveType(domain.getActiveType());

        try {
            stationJpaRepository.flush();
        } catch (DataIntegrityViolationException e) {
            throw CustomException.app(AppErrorCode.STATION_NAME_DUPLICATED)
                    .addParam("name", domain.getName());
        }
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
