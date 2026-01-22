package com.example.app.api.station.application;

import com.example.app.api.station.api.dto.request.CreateStationRequest;
import com.example.app.api.station.api.dto.request.DeleteStationRequest;
import com.example.core.business.station.Station;
import com.example.core.business.station.StationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StationService {

    private final StationRepository stationRepository;

    @Transactional
    public void createStation(CreateStationRequest request) {
        Station station = Station.create(request.getName());
        stationRepository.save(station);
    }

    @Transactional
    public void updateStationActivate(CreateStationRequest request) {
        Station station = Station.create(request.getName());
        return;
    }

    // 수정필요 line_station이 inactive 일때만 가능하도록
    // 이거 진짜 이상한데 수정하자
    @Transactional
    public void deleteStation(DeleteStationRequest request) {
        Station station = Station.create(request.getName());
        // 모든 segment 조회
    }
}
