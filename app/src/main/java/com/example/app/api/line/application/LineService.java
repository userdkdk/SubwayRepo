package com.example.app.api.line.application;

import com.example.app.api.line.api.dto.request.CreateLineRequest;
import com.example.app.api.line.api.dto.request.CreateSegmentRequest;
import com.example.app.api.line.api.dto.request.UpdateLineAttributeRequest;
import com.example.app.api.line.api.dto.request.UpdateLineStatusRequest;
import com.example.app.common.exception.AppErrorCode;
import com.example.app.common.redis.service.RedisLineService;
import com.example.app.common.redis.service.RedisSegmentService;
import com.example.app.common.response.enums.ActionType;
import com.example.app.common.response.enums.StatusFilter;
import com.example.core.business.line.Line;
import com.example.core.business.line.LineName;
import com.example.core.business.line.LineRepository;
import com.example.core.business.segment.Segment;
import com.example.core.business.segment.SegmentAttribute;
import com.example.core.business.segment.SegmentRepository;
import com.example.core.business.segmentHistory.SegmentHistory;
import com.example.core.business.segmentHistory.SegmentHistoryRepository;
import com.example.core.business.station.StationRepository;
import com.example.core.business.station.StationRoleInLine;
import com.example.core.exception.CustomException;
import com.example.core.exception.ErrorCode;
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
    private final RedisLineService redisLineService;

    @Transactional
    public void createLine(CreateLineRequest request) {
        Integer startId = request.startId();
        Integer endId = request.endId();
        Double distance = request.distance();
        Integer spendTime = request.spendTime();
        // check station exists and isActive
        checkStationExists(startId);
        checkStationExists(endId);
        // create line
        LineName name = new LineName(request.name());
        lineRepository.ensureNameUnique(name.value());
        Line savedLine = lineRepository.save(Line.create(name));

        // create segment
        saveSegment(savedLine.getId(), startId, endId, distance, spendTime);
    }
    @Transactional
    public void addStation(Integer lineId, CreateSegmentRequest request) {
        Integer stationId = request.stationId();
        Integer beforeId = request.beforeId();
        Integer afterId = request.afterId();
        Double beforeDistance = request.beforeDistance();
        Integer beforeSpendTime = request.beforeSpendTime();
        Double afterDistance = request.afterDistance();
        Integer afterSpendTime = request.afterSpendTime();

        // check line and station exists
        checkLineExists(lineId);
        checkStationExists(stationId);
        // check station exists in line -> duplicated
        if (segmentRepository.existsActiveStationInLine(lineId, stationId)) {
            throw CustomException.app(AppErrorCode.STATION_ALREADY_EXISTS_IN_LINE)
                    .addParam("line id", lineId)
                    .addParam("station id", stationId);
        }

        // Delete cache
        redisSegmentService.evictPath();
        redisLineService.evictSegments(lineId, StatusFilter.ALL);

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
    public void updateLineAttribute(Integer id, UpdateLineAttributeRequest request) {
        LineName name = new LineName(request.name());
        lineRepository.ensureNameUnique(name.value());
        lineRepository.update(id, line -> {line.changeName(name);});
    }

    @Transactional
    public void updateLineStatus(Integer id, UpdateLineStatusRequest request) {
        lineRepository.update(id, line -> {
            ActionType action = request.actionType();
            if (segmentRepository.existsActiveSegmentByLine(id)) {
                throw CustomException.app(AppErrorCode.ACTIVE_LINE_EXISTS)
                        .addParam("id", id);
            }
            line.changeActiveType(action.toActiveType());
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
