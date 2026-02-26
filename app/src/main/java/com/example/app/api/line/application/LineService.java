package com.example.app.api.line.application;

import com.example.app.api.line.api.dto.request.line.CreateLineRequest;
import com.example.app.api.line.api.dto.request.line.UpdateLineAttributeRequest;
import com.example.app.api.line.api.dto.request.line.ActivateLineRequest;
import com.example.app.common.exception.AppErrorCode;
import com.example.app.common.dto.request.enums.ActionType;
import com.example.core.business.line.Line;
import com.example.core.business.line.LineName;
import com.example.core.business.line.LineRepository;
import com.example.core.business.segment.Segment;
import com.example.core.business.segment.SegmentAttribute;
import com.example.core.business.segment.SegmentRepository;
import com.example.core.business.segmentHistory.SegmentHistory;
import com.example.core.business.segmentHistory.SegmentHistoryRepository;
import com.example.core.business.station.StationRepository;
import com.example.core.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LineService {

    private final LineRepository lineRepository;
    private final StationRepository stationRepository;
    private final SegmentRepository segmentRepository;
    private final SegmentHistoryRepository segmentHistoryRepository;

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void createLine(CreateLineRequest request) {
        Integer startId = request.startId();
        Integer endId = request.endId();
        Double distance = request.attribute().distance();
        Integer spendTime = request.attribute().spendTime();
        // check station exists and isActive
        checkStationExists(startId);
        checkStationExists(endId);
        // create line
        LineName name = new LineName(request.name());
        Line savedLine = lineRepository.save(Line.create(name));

        // upsert segment
        upsertSegment(savedLine.getId(), startId, endId, distance, spendTime);
    }


    @Transactional
    public void updateLineAttribute(Integer id, UpdateLineAttributeRequest request) {
        LineName name = new LineName(request.name());
        lineRepository.updateAttribute(id, name);
    }

    @Transactional
    public void activateLine(Integer id, ActivateLineRequest request) {
        Integer startId = request.startId();
        Integer endId = request.endId();
        Double distance = request.attribute().distance();
        Integer spendTime = request.attribute().spendTime();
        // check station exists and isActive
        checkStationExists(startId);
        checkStationExists(endId);

        lineRepository.activeLine(id);
        // upsert segment
        upsertSegment(id, startId, endId, distance, spendTime);
    }

    @Transactional
    public void deactivateLine(Integer id) {
        segmentRepository.inactivateByLineId(id);
        lineRepository.deactiveLine(id);
    }

    private void upsertSegment(Integer id, Integer startId, Integer endId, Double distance, Integer spendTime) {
        SegmentAttribute segmentAttribute = new SegmentAttribute(distance, spendTime);
        Segment segment = Segment.create(id, startId, endId, segmentAttribute);
        segmentRepository.upsert(segment);
        Integer segmentId = segmentRepository.findIdByUniqueKey(segment);
        segmentHistoryRepository.save(SegmentHistory.create(segmentId));
    }

    private void checkStationExists(Integer id) {
        if (!stationRepository.existsActiveById(id)) {
            throw CustomException.domain(AppErrorCode.STATION_NOT_FOUND)
                    .addParam("id", id);
        }
    }
}
