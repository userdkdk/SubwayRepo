package com.example.app.api.station.application;

import com.example.app.api.station.adapter.StationApiMapper;
import com.example.app.api.station.api.dto.request.CreateStationRequest;
import com.example.app.api.station.api.dto.request.DeleteStationRequest;
import com.example.app.api.station.api.dto.response.StationResponse;
import com.example.app.business.segment.SegmentQueryRepository;
import com.example.app.business.station.StationQueryRepository;
import com.example.core.business.station.Station;
import com.example.core.business.station.StationRepository;
import com.example.core.common.exception.CustomException;
import com.example.core.common.exception.DomainErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StationService {

    private final StationRepository stationRepository;
    private final StationQueryRepository stationQueryRepository;
    private final SegmentQueryRepository segmentQueryRepository;
    private final StationApiMapper stationMapper;

    public List<StationResponse> getAllStation() {
        return stationQueryRepository.findAllByActive().stream()
                .map(stationMapper::entityToDto)
                .toList();
    }

    @Transactional
    public void createStation(CreateStationRequest request) {
        Station station = Station.create(request.getName());
        stationRepository.save(station);
    }

    @Transactional
    public void updateStationActivate(CreateStationRequest request) {
        Station station = Station.create(request.getName());
        stationRepository.activate(station);
    }

    // 수정필요 line_station이 inactive 일때만 가능하도록
    @Transactional
    public void deleteStation(DeleteStationRequest request) {
        Station station = Station.create(request.getName());
        // 모든 segment 조회
        if (segmentQueryRepository.existsByStationId(station)) {
            throw CustomException.domain(DomainErrorCode.STATION_DELETE_ERROR)
                    .addParam("station name", station.getName());
        }
        stationRepository.inActivate(station);
    }
}
