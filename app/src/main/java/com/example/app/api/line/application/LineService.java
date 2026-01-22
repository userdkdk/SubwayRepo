package com.example.app.api.line.application;

import com.example.app.api.line.api.dto.request.CreateLineRequest;
import com.example.app.api.line.api.dto.request.CreateSegmentRequest;
import com.example.app.api.line.api.dto.request.UpdateLineRequest;
import com.example.app.business.line.LineQueryRepository;
import com.example.app.business.segment.SegmentJpaEntity;
import com.example.app.business.segment.SegmentQueryRepository;
import com.example.app.business.station.StationQueryRepository;
import com.example.app.common.exception.AppErrorCode;
import com.example.core.business.line.Line;
import com.example.core.business.line.LineRepository;
import com.example.core.business.segment.Segment;
import com.example.core.business.segment.SegmentRepository;
import com.example.core.common.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LineService {

    private final LineRepository lineRepository;
    private final StationQueryRepository stationQueryRepository;
    private final SegmentRepository segmentRepository;
    private final LineQueryRepository lineQueryRepository;
    private final SegmentQueryRepository segmentQueryRepository;

    @Transactional
    public void createLine(CreateLineRequest request) {
        Integer startId = request.getStartId();
        Integer endId = request.getEndId();
        double distance = request.getDistance();
        int spendTIme = request.getSpendTime();
        // check stations
        checkStationExists(startId);
        checkStationExists(endId);
        // create line
        Line line = Line.create(request.getName());
        Line savedLine = lineRepository.save(line);

        // create segment
        Segment segment = Segment.create(savedLine.getId(), startId, endId,
                distance, spendTIme);
        segmentRepository.save(segment);
    }

    @Transactional
    public void addStation(Integer lineId, CreateSegmentRequest request) {
        Integer stationId = request.getStationId();
        Integer beforeId = request.getBeforeId();
        Integer afterId = request.getAfterId();
        double beforeDistance = request.getBeforeDistance();
        int beforeSpendTIme = request.getBeforeSpendTime();
        double afterDistance = request.getAfterDistance();
        int afterSpendTIme = request.getAfterSpendTime();

        // check line and station exists
        checkLineExists(lineId);
        checkStationExists(stationId);

        // station already exists in line
        segmentQueryRepository.ensureStationNotInLine(lineId, stationId);

        if (beforeId==null && afterId!=null) {
            // check end id
            segmentQueryRepository.ensureIsHead(lineId, afterId);
            // save
            Segment segment = Segment.create(lineId, stationId, afterId, afterDistance, afterSpendTIme);
            segmentRepository.save(segment);
            return;
        }
        if (beforeId!=null && afterId==null) {
            // check start id
            segmentQueryRepository.ensureIsTail(lineId, beforeId);
            // save
            Segment segment = Segment.create(lineId, beforeId, stationId, beforeDistance, beforeSpendTIme);
            segmentRepository.save(segment);
            return;
        }
        if (beforeId!=null && afterId!=null) {
            // find seg
            SegmentJpaEntity cur = segmentQueryRepository
                    .findActiveByLineAndStation(lineId, beforeId, afterId)
                    .orElseThrow(()->CustomException.app(AppErrorCode.SEGMENT_NOT_FOUND)
                            .addParam("line id", lineId)
                            .addParam("start id", beforeId)
                            .addParam("end id", afterId));
            cur.inActivate();

            Segment s1 = Segment.create(lineId, beforeId, stationId, beforeDistance, beforeSpendTIme);
            Segment s2 = Segment.create(lineId, stationId, afterId, afterDistance, afterSpendTIme);
            segmentRepository.save(s1);
            segmentRepository.save(s2);
            return;
        }
        throw CustomException.app(AppErrorCode.SEGMENT_INPUT_VALUE_ERROR,
                "startId, endId input이 올바르지 않습니다.");
    }

    @Transactional
    public void updateLine(Integer lineId, UpdateLineRequest request) {
        lineRepository.update(lineId, line -> {
            if (request.getName()!=null) {
                line.changeName(request.getName());
            }
            if (request.getActiveType()!=null) {
                line.changeActiveType(request.getActiveType());
            }
        });
    }

    private void checkLineExists(Integer id) {
        if (!lineQueryRepository.existsActiveById(id)) {
            throw CustomException.domain(AppErrorCode.LINE_NOT_FOUND)
                    .addParam("id", id);
        }
    }

    private void checkStationExists(Integer id) {
        if (!stationQueryRepository.existsActiveById(id)) {
            throw CustomException.domain(AppErrorCode.STATION_NOT_FOUND)
                    .addParam("id", id);
        }
    }

}
