package com.example.db.business.station;

import com.example.core.common.exception.DomainErrorCode;
import com.example.core.domain.station.Station;
import com.example.core.domain.station.StationRepository;
import com.example.core.common.domain.enums.ActiveType;
import com.example.core.common.exception.CustomException;
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
            stationJpaRepository.flush();
        } catch (DataIntegrityViolationException e) {
            throw CustomException.app(DomainErrorCode.STATION_NAME_DUPLICATED)
                    .addParam("name", station.getName());
        }
    }

    @Override
    public void update(Integer id, Consumer<Station> updater) {
        // get station
        StationJpaEntity entity = stationJpaRepository.findById(id)
                .orElseThrow(()->CustomException.app(DomainErrorCode.STATION_NOT_FOUND)
                        .addParam("id", id));
        Station domain = stationMapper.toDomain(entity);
        updater.accept(domain);

        entity.changeName(domain.getName());
        entity.changeActiveType(domain.getActiveType());

        try {
            stationJpaRepository.flush();
        } catch (DataIntegrityViolationException e) {
            throw CustomException.app(DomainErrorCode.STATION_NAME_DUPLICATED)
                    .addParam("name", domain.getName());
        }
    }

    @Override
    public boolean existsActiveById(Integer id) {
        return stationJpaRepository.existsByIdAndActiveType(id, ActiveType.ACTIVE);
    }
}
