package com.example.app.api.line.application;

import com.example.app.api.line.api.dto.request.CreateLineRequest;
import com.example.app.api.station.api.dto.response.StationResponse;
import com.example.app.business.line.LineQueryRepository;
import com.example.app.business.segment.SegmentJpaEntity;
import com.example.app.business.segment.SegmentQueryRepository;
import com.example.app.business.station.StationQueryRepository;
import com.example.core.business.line.Line;
import com.example.core.business.line.LineRepository;
import com.example.core.business.segment.Segment;
import com.example.core.business.segment.SegmentRepository;
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
public class LineService {

    private final LineRepository lineRepository;
    private final LineQueryRepository lineQueryRepository;
    private final StationRepository stationRepository;
    private final StationQueryRepository stationQueryRepository;
    private final SegmentRepository segmentRepository;
    private final SegmentQueryRepository segmentQueryRepository;

    public List<StationResponse> getStationsById(Integer lineId) {

        if (!lineQueryRepository.existsById(lineId)) {
            throw CustomException.domain(DomainErrorCode.LINE_NOT_FOUND)
                    .addParam("line id",lineId);
        }
        List<SegmentJpaEntity> segments = segmentQueryRepository.findByLine(lineId);

    }

    @Transactional
    public void createLine(CreateLineRequest request) {
        Integer startId = request.getStartId();
        Integer endId = request.getEndId();
        double distance = request.getDistance();
        int spendTIme = request.getSpendTime();
        // check stations
        if (!stationQueryRepository.existsById(startId)) {
            throw CustomException.domain(DomainErrorCode.STATION_NOT_FOUND)
                    .addParam("id", startId);
        }
        if (!stationQueryRepository.existsById(endId)) {
            throw CustomException.domain(DomainErrorCode.STATION_NOT_FOUND)
                    .addParam("id", endId);
        }
        // create line
        Line line = Line.create(request.getName());
        Line savedLine = lineRepository.save(line);

        // create segment
        Segment segment = Segment.create(savedLine.getId(), startId, endId,
                distance, spendTIme);
        segmentRepository.save(segment);
    }
}
