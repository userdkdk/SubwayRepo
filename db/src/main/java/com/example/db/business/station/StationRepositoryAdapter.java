package com.example.db.business.station;

import com.example.app.common.exception.AppErrorCode;
import com.example.core.common.exception.DomainErrorCode;
import com.example.core.common.exception.ErrorCode;
import com.example.core.domain.station.Station;
import com.example.core.domain.station.StationName;
import com.example.core.domain.station.StationRepository;
import com.example.core.common.domain.enums.ActiveType;
import com.example.core.common.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import java.util.List;

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
    public Station findByIdAndActiveTypeForUpdate(Integer id, ActiveType activeType) {
        StationJpaEntity entity = stationJpaRepository.findByIdAndActivateForUpdate(id, activeType)
                .orElseThrow(()->CustomException.app(DomainErrorCode.STATION_NOT_FOUND)
                        .addParam("id", id));
        return stationMapper.toDomain(entity);
    }

    @Override
    public void findAllByIdsAndActiveTypeForUpdate(List<Integer> ids, ActiveType activeType) {
        List<StationJpaEntity> entities = stationJpaRepository.findAllByIdsAndActiveTypeForUpdate(ids, activeType);
        if (entities.size() != ids.size()) {
            throw CustomException.app(AppErrorCode.STATION_COUNTS_CONFLICT);
        }
    }

    @Override
    public void updateName(Integer id, StationName name) {
        StationJpaEntity entity = findById(id);
        entity.changeName(name.value());
        tryCommit(DomainErrorCode.STATION_NAME_DUPLICATED, name.value());
    }

    @Override
    public void updateStatus(Integer id, ActiveType activeType) {
        // same tx, lock already acquired
        StationJpaEntity entity = findById(id);
        entity.changeActiveType(activeType);
    }

    @Override
    public boolean existsActiveById(Integer id) {
        return stationJpaRepository.existsByIdAndActiveType(id, ActiveType.ACTIVE);
    }

    private StationJpaEntity findById(Integer id) {
        return stationJpaRepository.findById(id)
                .orElseThrow(()->CustomException.app(DomainErrorCode.STATION_NOT_FOUND)
                        .addParam("id", id));
    }

    private void tryCommit(ErrorCode code, String message) {
        try {
            stationJpaRepository.flush();
        } catch (DataIntegrityViolationException e) {
            throw CustomException.app(code,message);
        }
    }
}
