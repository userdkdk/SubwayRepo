package com.example.app.business.station.application;

import com.example.app.business.station.adapter.StationMapper;
import com.example.app.business.station.adapter.StationQueryRepository;
import com.example.app.business.station.api.dto.request.CreateStationRequest;
import com.example.app.business.station.api.dto.request.DeleteStationRequest;
import com.example.app.business.station.api.dto.response.StationResponse;
import com.example.core.business.station.StationRepository;
import com.example.core.common.domain.enums.ActiveType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StationService {

    private final StationRepository stationRepository;
    private final StationQueryRepository stationQueryRepository;
    private final StationMapper stationMapper;

    public List<StationResponse> getAllStation() {
        return stationQueryRepository.findAllByActive().stream()
                .map(stationMapper::entityToDto)
                .toList();
    }

    @Transactional
    public void createStation(CreateStationRequest request) {
        stationRepository.upsertActivateByName(request.getName());
    }

    public void getAllStations() {

    }

    @Transactional
    public void deleteStation(DeleteStationRequest request) {
        stationRepository.inActivateByName(request.getName());
    }
}
