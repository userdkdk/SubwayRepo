package com.example.app.business.station;

import com.example.app.common.exception.AppErrorCode;
import com.example.app.common.response.enums.StatusFilter;
import com.example.core.common.domain.enums.ActiveType;
import com.example.core.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StationQueryRepository {
    private final SpringDataStationJpaRepository stationJpaRepository;

    public List<StationJpaEntity> findByActiveType(StatusFilter status) {
        if (status != StatusFilter.ALL) {
            return stationJpaRepository.findByActiveType(status.toActiveType());
        }
        return stationJpaRepository.findAll();
    }

    public StationJpaEntity findById(Integer stationId) {
        return stationJpaRepository.findByIdAndActiveType(stationId, ActiveType.ACTIVE)
                .orElseThrow(()-> CustomException.app(AppErrorCode.STATION_NOT_FOUND));
    }
}
