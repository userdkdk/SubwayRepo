package com.example.app.business.station;

import com.example.core.common.domain.enums.ActiveType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StationQueryRepository {
    private final SpringDataStationJpaRepository stationJpaRepository;

    public List<StationJpaEntity> findAllByActive() {
        return stationJpaRepository.findAllByActiveType(ActiveType.ACTIVE);
    }

    public boolean existsById(Integer id) {
        return stationJpaRepository.existsById(id);
    }
}
