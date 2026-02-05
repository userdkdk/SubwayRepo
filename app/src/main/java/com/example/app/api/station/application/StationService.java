package com.example.app.api.station.application;

import com.example.app.api.station.api.dto.request.CreateStationRequest;
import com.example.app.api.station.api.dto.request.UpdateStationRequest;
import com.example.app.common.exception.AppErrorCode;
import com.example.app.common.response.enums.StatusFilter;
import com.example.core.business.segment.SegmentRepository;
import com.example.core.business.station.Station;
import com.example.core.business.station.StationRepository;
import com.example.core.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StationService {

    private final StationRepository stationRepository;
    private final SegmentRepository segmentRepository;

    @Transactional
    public void createStation(CreateStationRequest request) {
        Station station = Station.create(request.getName());
        stationRepository.save(station);
    }

    @Transactional
    public void updateStation(Integer id, UpdateStationRequest request) {
        // check station exists
        if (!stationRepository.existsById(id)) {
            throw CustomException.app(AppErrorCode.STATION_NOT_FOUND)
                    .addParam("id", id);
        }
        // update station
        stationRepository.update(id, station->{
            if (request.getStatus()!=null) {
                // check segment when change to inactive
                if (request.getStatus()== StatusFilter.INACTIVE &&
                        segmentRepository.existsActiveStation(id)) {
                    throw CustomException.app(AppErrorCode.ACTIVE_STATION_EXISTS)
                            .addParam("id", id);
                }


                station.changeActiveType(request.getStatus().toActiveType());
            }
            if (request.getName()!=null && !request.getName().isBlank()) {
                station.changeName(request.getName());
            }
        });
    }

}
