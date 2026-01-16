package com.example.app.business.line.application;

import com.example.app.business.line.api.dto.CreateLineRequest;
import com.example.app.common.exception.AppErrorCode;
import com.example.core.business.line.Line;
import com.example.core.business.line.LineRepository;
import com.example.core.business.lineStation.LineStation;
import com.example.core.business.lineStation.LineStationRepository;
import com.example.core.business.station.StationRepository;
import com.example.core.common.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LineService {

    private final LineRepository lineRepository;
    private final StationRepository stationRepository;
    private final LineStationRepository lineStationRepository;

    @Transactional
    public void createLine(CreateLineRequest request) {
        // check station
        Integer start = stationRepository.findIdByName(request.getStartStation())
                .orElseThrow(()->CustomException.app(AppErrorCode.STATION_NOT_FOUND));
        Integer end = stationRepository.findIdByName(request.getEndStation())
                .orElseThrow(()->CustomException.app(AppErrorCode.STATION_NOT_FOUND));

    }
}
