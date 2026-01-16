package com.example.app.business.station.application;

import com.example.app.business.station.adapter.StationJpaEntity;
import com.example.app.business.station.adapter.StationMapper;
import com.example.app.business.station.adapter.StationQueryRepository;
import com.example.app.business.station.api.dto.CreateStationRequest;
import com.example.app.common.exception.AppErrorCode;
import com.example.core.business.station.Station;
import com.example.core.business.station.StationRepository;
import com.example.core.common.exception.CustomException;
import com.example.core.common.exception.DomainErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
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

    @Transactional
    public void createStation(CreateStationRequest request) {
        // check domain
        stationRepository.upsertActivateByName(request.getName());
    }

    public void getAllStations() {

    }
}
