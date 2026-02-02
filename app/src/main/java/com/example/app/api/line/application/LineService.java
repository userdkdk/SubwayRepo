package com.example.app.api.line.application;

import com.example.app.api.line.api.dto.request.CreateLineRequest;
import com.example.app.api.line.api.dto.request.CreateSegmentRequest;
import com.example.app.api.line.api.dto.request.UpdateLineRequest;
import com.example.app.common.exception.AppErrorCode;
import com.example.app.common.redis.service.RedisSegmentService;
import com.example.core.business.line.Line;
import com.example.core.business.line.LineRepository;
import com.example.core.business.segment.Segment;
import com.example.core.business.segment.SegmentAttribute;
import com.example.core.business.segment.SegmentRepository;
import com.example.core.business.segmentHistory.SegmentHistory;
import com.example.core.business.segmentHistory.SegmentHistoryRepository;
import com.example.core.business.station.StationRepository;
import com.example.core.business.station.StationRoleInLine;
import com.example.core.common.exception.CustomException;
import com.example.core.common.exception.ErrorCode;
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
    private final StationRepository stationRepository;
    private final SegmentRepository segmentRepository;
    private final SegmentHistoryRepository segmentHistoryRepository;
    private final RedisSegmentService redisSegmentService;

    @Transactional
    public void createLine(CreateLineRequest request) {
        Integer startId = request.getStartId();
        Integer endId = request.getEndId();
        Double distance = request.getDistance();
        Integer spendTime = request.getSpendTime();
        // check stations
        checkStationExists(startId);
        checkStationExists(endId);
        // create line
        Line line = Line.create(request.getName());
        Line savedLine = lineRepository.save(line);

        // create segment
        saveSegment(savedLine.getId(), startId, endId, distance, spendTime);
    }
    @Transactional
    public void addStation(Integer lineId, CreateSegmentRequest request) {
        Integer stationId = request.getStationId();
        Integer beforeId = request.getBeforeId();
        Integer afterId = request.getAfterId();
        Double beforeDistance = request.getBeforeDistance();
        Integer beforeSpendTime = request.getBeforeSpendTime();
        Double afterDistance = request.getAfterDistance();
        Integer afterSpendTime = request.getAfterSpendTime();

        // check line and station exists
        checkLineExists(lineId);
        checkStationExists(stationId);
        redisSegmentService.evictPath();
        // check station exists in line
        if (segmentRepository.existsActiveStationInLine(lineId, stationId)) {
            throw CustomException.app(AppErrorCode.STATION_ALREADY_EXISTS_IN_LINE)
                    .addParam("line id", lineId)
                    .addParam("station id", stationId);
        }

        if (beforeId==null && afterId!=null) {
            // check domain
            checkDistAndTime(afterDistance, afterSpendTime);
            // check end id
            ensureHead(lineId, afterId);
            // save
            saveSegment(lineId, stationId, afterId, afterDistance, afterSpendTime);
            return;
        }
        if (beforeId!=null && afterId==null) {
            // check domain
            checkDistAndTime(beforeDistance, beforeSpendTime);
            // check start id
            ensureTail(lineId, beforeId);
            // save
            saveSegment(lineId, beforeId, stationId, beforeDistance, beforeSpendTime);
            return;
        }
        if (beforeId!=null && afterId!=null) {
            // check domain
            checkDistAndTime(beforeDistance, beforeSpendTime);
            checkDistAndTime(afterDistance, afterSpendTime);

            // inactivate seg
            int updated = segmentRepository
                    .inactivateActiveSegment(lineId, beforeId, afterId);

            if (updated == 0) {
                throw CustomException.app(AppErrorCode.SEGMENT_NOT_FOUND)
                        .addParam("line id", lineId)
                        .addParam("start id", beforeId)
                        .addParam("end id", afterId);
            }

            // save
            saveSegment(lineId, beforeId, stationId, beforeDistance, beforeSpendTime);
            saveSegment(lineId, stationId, afterId, afterDistance, afterSpendTime);
            return;
        }

        throw CustomException.app(AppErrorCode.SEGMENT_INPUT_VALUE_ERROR,
                "startId, endId input이 올바르지 않습니다.");
    }

    @Transactional
    public void updateLine(Integer lineId, UpdateLineRequest request) {
        lineRepository.update(lineId, line -> {
            if (request.getName()!=null && !request.getName().isBlank()) {
                line.changeName(request.getName());
            }
            if (request.getStatus()!=null) {
                line.changeActiveType(request.getStatus().toActiveType());
            }
        });
    }

    private void saveSegment(Integer id, Integer startId, Integer endId, Double distance, Integer spendTime) {
        SegmentAttribute segmentAttribute = new SegmentAttribute(distance, spendTime);
        Segment segment = Segment.create(id, startId, endId,
                segmentAttribute);
        Integer segmentId = segmentRepository.save(segment);
        segmentHistoryRepository.save(SegmentHistory.create(segmentId));
    }

    private void ensureHead(Integer lineId, Integer stationId) {
        StationRoleInLine role = segmentRepository.findActiveRole(lineId, stationId);

        if (role == StationRoleInLine.NOT_IN_LINE) {
            throwExceptionByLineIdAndStationId(AppErrorCode.SEGMENT_NOT_FOUND, lineId, stationId);
        }
        if (role != StationRoleInLine.HEAD) {
            throwExceptionByLineIdAndStationId(AppErrorCode.SEGMENT_ALREADY_EXISTS, lineId, stationId);
        }
    }

    private void ensureTail(Integer lineId, Integer stationId) {
        StationRoleInLine role = segmentRepository.findActiveRole(lineId, stationId);

        if (role == StationRoleInLine.NOT_IN_LINE) {
            throwExceptionByLineIdAndStationId(AppErrorCode.SEGMENT_NOT_FOUND, lineId, stationId);
        }
        if (role != StationRoleInLine.TAIL) {
            throwExceptionByLineIdAndStationId(AppErrorCode.SEGMENT_ALREADY_EXISTS, lineId, stationId);
        }
    }

    private void checkLineExists(Integer id) {
        if (!lineRepository.existsActiveById(id)) {
            throw CustomException.domain(AppErrorCode.LINE_NOT_FOUND)
                    .addParam("id", id);
        }
    }

    private void checkStationExists(Integer id) {
        if (!stationRepository.existsActiveById(id)) {
            throw CustomException.domain(AppErrorCode.STATION_NOT_FOUND)
                    .addParam("id", id);
        }
    }
    private void checkDistAndTime(Double distance, Integer spendTime) {
        if (distance==null || spendTime==null) {
            throw CustomException.app(AppErrorCode.SEGMENT_INPUT_VALUE_ERROR,
                    "Input invalid segment Attribute");
        }
    }
    private void throwExceptionByLineIdAndStationId(ErrorCode errorCode, Integer lineId, Integer stationId) {
        throw CustomException.app(errorCode)
                .addParam("line id", lineId)
                .addParam("station id", stationId);
    }
}
