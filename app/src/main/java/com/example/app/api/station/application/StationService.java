package com.example.app.api.station.application;

import com.example.app.api.station.api.dto.request.CreateStationRequest;
import com.example.app.api.station.api.dto.request.UpdateStationAttributeRequest;
import com.example.app.api.station.api.dto.request.UpdateStationStatusRequest;
import com.example.app.common.exception.AppErrorCode;
import com.example.app.common.dto.request.enums.ActionType;
import com.example.core.common.domain.enums.ActiveType;
import com.example.core.domain.segment.SegmentRepository;
import com.example.core.domain.station.Station;
import com.example.core.domain.station.StationName;
import com.example.core.domain.station.StationRepository;
import com.example.core.common.exception.CustomException;
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
        // station create
        StationName name = new StationName(request.name());
        stationRepository.save(Station.create(name));
    }

    @Transactional
    public void updateStationName(Integer id, UpdateStationAttributeRequest request) {
        // update station
        StationName name = new StationName(request.name());
        stationRepository.updateName(id, name);
    }

    @Transactional
    public void updateStationStatus(Integer id, UpdateStationStatusRequest request) {
        ActiveType target = request.actionType().toActiveType();
        // station lock for update
        Station station = stationRepository.findByIdForUpdate(id);
        // return if status not change;
        if (station.getActiveType() == target) {
            return;
        }
        if (target== ActiveType.INACTIVE &&
                segmentRepository.existsActiveSegmentByStation(id)) {
            throw CustomException.app(AppErrorCode.ACTIVE_STATION_EXISTS)
                    .addParam("id", id);
        }
        stationRepository.updateStatus(id, target);
    }
}
