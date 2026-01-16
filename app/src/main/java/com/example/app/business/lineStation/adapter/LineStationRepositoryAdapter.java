package com.example.app.business.lineStation.adapter;

import com.example.core.business.lineStation.LineStation;
import com.example.core.business.lineStation.LineStationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class LineStationRepositoryAdapter implements LineStationRepository {
    private final SpringDataLineStationJpaRepository lineStationJpaRepository;

    @Override
    public Optional<LineStation> findByLineIdAndStationId(Integer lineId, Integer stationId) {
        lineStationJpaRepository.findByLineJpaEntity_IdAndStationJpaEntity_Id(1,2);
        return null;
    }
}
