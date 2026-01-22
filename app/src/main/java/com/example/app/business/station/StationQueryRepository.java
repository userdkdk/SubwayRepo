package com.example.app.business.station;

import com.example.app.common.response.enums.StatusFilter;
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

    public boolean existsActiveById(Integer id) {
        return stationJpaRepository.existsByIdAndActiveType(id, ActiveType.ACTIVE);
    }

    public List<StationJpaEntity> findByActiveType(StatusFilter status) {
        if (status != StatusFilter.ALL) {
            return stationJpaRepository.findByActiveType(status.toActiveType());
        }
        return stationJpaRepository.findAll();
    }
}
