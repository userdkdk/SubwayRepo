package com.example.app.admin.line.application;

import com.example.app.admin.line.api.dto.CreateLineRequest;
import com.example.app.business.station.StationQueryRepository;
import com.example.core.business.line.LineRepository;
import com.example.core.business.lineStation.LineStationRepository;
import com.example.core.business.station.StationRepository;
import com.example.core.common.exception.CustomException;
import com.example.core.common.exception.DomainErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LineAdminService {

    private final LineRepository lineRepository;
    private final StationRepository stationRepository;
    private final StationQueryRepository stationQueryRepository;
    private final LineStationRepository lineStationRepository;

    @Transactional
    public void createLine(CreateLineRequest request) {
        Integer startId = request.getStartId();
        Integer endId = request.getEndId();
        // check stations
        if (stationQueryRepository.existsById(startId)) {
            throw CustomException.domain(DomainErrorCode.STATION_NOT_FOUND)
                    .addParam("id", startId);
        }
        if (stationQueryRepository.existsById(endId)) {
            throw CustomException.domain(DomainErrorCode.STATION_NOT_FOUND)
                    .addParam("id", endId);
        }
        // create line
        lineRepository.upsertActivateByName(request.getName(), startId, endId);

        // create line_station

        // create segment
    }
}
