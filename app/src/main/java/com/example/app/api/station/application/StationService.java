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
import org.springframework.util.StringUtils;

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
        // update station
        stationRepository.update(id, station->{
            StatusFilter newStatus = request.getStatus();
            String newName = request.getName();
            if (newStatus!=null && newStatus!=StatusFilter.ALL) {
                // check active segment when change to inactive
                if (newStatus== StatusFilter.INACTIVE &&
                        segmentRepository.existsActiveSegmentByStation(id)) {
                    throw CustomException.app(AppErrorCode.ACTIVE_STATION_EXISTS)
                            .addParam("id", id);
                }

                station.changeActiveType(newStatus.toActiveType());
            }
            if (StringUtils.hasText(newName)) {
                station.changeName(newName);
            }
        });
    }
}
