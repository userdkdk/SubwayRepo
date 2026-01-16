package com.example.app.business.station.adapter;

import com.example.core.common.domain.enums.ActiveType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class StationQueryRepository {
    private final SpringDataStationJpaRepository stationJpaRepository;

    public List<StationJpaEntity> findAllByActive() {
        return stationJpaRepository.findAllByActiveType(ActiveType.ACTIVE);
    }
}
