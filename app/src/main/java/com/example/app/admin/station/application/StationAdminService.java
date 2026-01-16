package com.example.app.admin.station.application;

import com.example.app.admin.station.adapter.StationApiMapper;
import com.example.app.admin.station.api.dto.request.CreateStationRequest;
import com.example.app.admin.station.api.dto.request.DeleteStationRequest;
import com.example.app.admin.station.api.dto.response.StationResponse;
import com.example.app.business.station.StationQueryRepository;
import com.example.core.business.station.StationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StationAdminService {

    private final StationRepository stationRepository;
    private final StationQueryRepository stationQueryRepository;
    private final StationApiMapper stationMapper;

    public List<StationResponse> getAllStation() {
        return stationQueryRepository.findAllByActive().stream()
                .map(stationMapper::entityToDto)
                .toList();
    }

    @Transactional
    public void createStation(CreateStationRequest request) {
        stationRepository.upsertActivateByName(request.getName());
    }

    // 수정필요 line_station이 inactive 일때만 가능하도록
    @Transactional
    public void deleteStation(DeleteStationRequest request) {
        stationRepository.inActivateByName(request.getName());
    }
}
