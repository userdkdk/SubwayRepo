package com.example.app.business.lineStation;

import com.example.app.business.line.LineJpaEntity;
import com.example.app.business.line.SpringDataLineJpaRepository;
import com.example.app.business.station.SpringDataStationJpaRepository;
import com.example.app.business.station.StationJpaEntity;
import com.example.core.business.lineStation.LineStation;
import com.example.core.business.lineStation.LineStationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class LineStationRepositoryAdapter implements LineStationRepository {
    private final SpringDataLineStationJpaRepository lineStationJpaRepository;
    private final SpringDataLineJpaRepository lineJpaRepository;
    private final SpringDataStationJpaRepository stationJpaRepository;
    private final LineStationMapper lineStationMapper;

    @Override
    public Optional<LineStation> findByLineIdAndStationId(Integer lineId, Integer stationId) {
        lineStationJpaRepository.findByLineJpaEntity_IdAndStationJpaEntity_Id(1,2);
        return null;
    }

    @Override
    public void save(LineStation lineStation) {
        LineJpaEntity lineRef = lineJpaRepository.getReferenceById(lineStation.getLineId());
        StationJpaEntity stationRef = stationJpaRepository.getReferenceById(lineStation.getStationId());

    }
}
