package com.example.app.business.station.application;

import com.example.app.business.station.api.dto.CreateStationRequest;
import com.example.core.business.station.Station;
import com.example.core.business.station.StationRepository;
import com.example.core.common.exception.CustomException;
import com.example.core.common.exception.DomainErrorCode;
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
        // check domain
        Station station = Station.create(request.getName());

        // check unique and save
        if (stationRepository.existsByName(station.getName())) {
            throw CustomException.domain(DomainErrorCode.STATION_NAME_DUPLICATED)
                    .addParam("name", station.getName());
        }
        stationRepository.save(station);
    }
}
