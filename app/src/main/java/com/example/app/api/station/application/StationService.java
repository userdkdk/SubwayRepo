package com.example.app.api.station.application;

import com.example.app.api.station.api.dto.request.CreateStationRequest;
import com.example.app.api.station.api.dto.request.UpdateStationRequest;
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
    public void updateStation(Integer id, UpdateStationRequest request) {
        stationRepository.update(id, station->{
            if (!request.getName().isBlank()) {
                station.changeName(request.getName());
            }
            if (request.getStatus()!=null) {
                station.changeActiveType(request.getStatus().toActiveType());
            }
        });
    }

}
