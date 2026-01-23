package com.example.app.api.station.application;

import com.example.app.api.station.adapter.StationApiMapper;
import com.example.app.api.station.api.dto.response.StationResponse;
import com.example.app.business.station.StationJpaEntity;
import com.example.app.business.station.StationQueryRepository;
import com.example.app.common.response.enums.StatusFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StationViewService {

    private final StationQueryRepository stationQueryRepository;
    private final StationApiMapper stationMapper;

    public List<StationResponse> getStations(StatusFilter status) {
        return stationQueryRepository.findByActiveType(status).stream()
                .map(stationMapper::entityToDto)
                .toList();
    }

    public StationResponse getStationById(Integer stationId) {
        StationJpaEntity entity = stationQueryRepository.findById(stationId);
        return stationMapper.entityToDto(entity);
    }
}
