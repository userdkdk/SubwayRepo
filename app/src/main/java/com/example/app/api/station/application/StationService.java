package com.example.app.api.station.application;

import com.example.app.api.station.api.dto.request.CreateStationRequest;
import com.example.app.api.station.api.dto.request.UpdateStationAttributeRequest;
import com.example.app.api.station.api.dto.request.UpdateStationStatusRequest;
import com.example.app.common.exception.AppErrorCode;
import com.example.app.common.dto.request.enums.ActionType;
import com.example.core.business.segment.SegmentRepository;
import com.example.core.business.station.Station;
import com.example.core.business.station.StationName;
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
        StationName name = new StationName(request.name());
        stationRepository.ensureNameUnique(name.value());
        stationRepository.save(Station.create(name));
    }

    @Transactional
    public void updateStationAttribute(Integer id, UpdateStationAttributeRequest request) {
        // update station
        StationName name = new StationName(request.name());
        stationRepository.ensureNameUnique(name.value());
        stationRepository.update(id, station->station.changeName(name));
    }

    @Transactional
    public void updateStationStatus(Integer id, UpdateStationStatusRequest request) {
        // update station
        ActionType action = request.actionType();
        if (action== ActionType.INACTIVE &&
                segmentRepository.existsActiveSegmentByStation(id)) {
            throw CustomException.app(AppErrorCode.ACTIVE_STATION_EXISTS)
                    .addParam("id", id);
        }
        stationRepository.update(id, station->station.changeActiveType(action.toActiveType()));
    }
}
