package com.example.app.api.line.application;

import com.example.app.api.line.api.dto.request.segment.CreateSegmentRequest;
import com.example.app.api.line.api.dto.request.segment.RemoveStationRequest;
import com.example.app.common.exception.AppErrorCode;
import com.example.core.common.exception.DomainErrorCode;
import com.example.core.domain.segment.Position;
import com.example.db.common.redis.service.LineSegmentChangedEvent;
import com.example.core.domain.line.LineRepository;
import com.example.core.domain.segment.Segment;
import com.example.core.domain.segment.SegmentAttribute;
import com.example.core.domain.segment.SegmentRepository;
import com.example.core.domain.segmentHistory.SegmentHistory;
import com.example.core.domain.segmentHistory.SegmentHistoryRepository;
import com.example.core.domain.station.StationRepository;
import com.example.core.domain.station.StationRoleInLine;
import com.example.core.common.exception.CustomException;
import com.example.core.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LineSegmentService {

    private final LineRepository lineRepository;
    private final StationRepository stationRepository;
    private final SegmentRepository segmentRepository;
    private final SegmentHistoryRepository segmentHistoryRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public void addStation(Integer lineId, Integer stationId, CreateSegmentRequest request) {
        Integer beforeId = request.beforeId();
        Integer afterId = request.afterId();
        Double beforeDistance = request.before().distance();
        Integer beforeSpendTime = request.before().spendTime();
        Double afterDistance = request.after().distance();
        Integer afterSpendTime = request.after().spendTime();

        // force increment line
        lineRepository.ensureExistsForUpdate(lineId);
        // check line and station exists
        checkStationExists(stationId);

        // check station exists in line -> duplicated
        if (segmentRepository.existsActiveStationInLine(lineId, stationId)) {
            throw CustomException.app(DomainErrorCode.STATION_ALREADY_EXISTS_IN_LINE)
                    .addParam("line id", lineId)
                    .addParam("station id", stationId);
        }

        Position pos = resolveRole(beforeId, afterId);

        switch (pos) {
            case HEAD -> {
                // check after is head
                StationRoleInLine actual = actualRole(lineId, afterId);
                ensureRole(lineId, afterId, pos, actual);
                // save
                upsertSegment(lineId, stationId, afterId, afterDistance, afterSpendTime);
            }
            case TAIL -> {
                // check before is tail
                StationRoleInLine actual = actualRole(lineId, beforeId);
                ensureRole(lineId, beforeId, pos, actual);
                // save
                upsertSegment(lineId, beforeId, stationId, beforeDistance, beforeSpendTime);
            }
            case INTERNAL -> {
                // inactivate segment
                ensureInactivateSegment(lineId, beforeId, afterId);

                // save
                upsertSegment(lineId, beforeId, stationId, beforeDistance, beforeSpendTime);
                upsertSegment(lineId, stationId, afterId, afterDistance, afterSpendTime);
            }
            default -> {
                throw CustomException.app(AppErrorCode.SEGMENT_INPUT_VALUE_ERROR,
                        "startId, endId input이 올바르지 않습니다.");
            }
        }
        // delete cache
        eventPublisher.publishEvent(new LineSegmentChangedEvent(lineId));
    }

    @Transactional
    public void removeStationInLine(Integer lineId, Integer stationId, RemoveStationRequest request) {
        Integer beforeId = request.beforeId();
        Integer afterId = request.afterId();
        Double distance = request.merged().distance();
        Integer spendTime = request.merged().spendTime();

        // force increment line
        lineRepository.ensureExistsForUpdate(lineId);
        // check line and station exists
        checkStationExists(stationId);

        // check station exists
        if (!segmentRepository.existsActiveStationInLine(lineId, stationId)) {
            throw CustomException.app(DomainErrorCode.STATION_NOT_EXISTS_IN_LINE)
                    .addParam("line id", lineId)
                    .addParam("station id", stationId);
        }

        Position pos = resolveRole(beforeId, afterId);
        StationRoleInLine actual = actualRole(lineId, stationId);
        ensureRole(lineId, stationId, pos, actual);

        switch (pos) {
            case HEAD -> {
                // inactive head
                ensureInactivateSegment(lineId,stationId,afterId);
                break;
            }
            case TAIL -> {
                // inactive tail
                ensureInactivateSegment(lineId, beforeId, stationId);
                break;
            }
            case INTERNAL -> {
                // inactivate before and after
                ensureInactivateSegment(lineId, beforeId, stationId);
                ensureInactivateSegment(lineId, stationId, afterId);

                // update or save
                upsertSegment(lineId, beforeId, afterId, distance, spendTime);
            }
            default -> {
                throw CustomException.app(AppErrorCode.SEGMENT_INPUT_VALUE_ERROR,
                        "startId, endId input이 올바르지 않습니다.");
            }
        }
        // delete cache
        eventPublisher.publishEvent(new LineSegmentChangedEvent(lineId));
    }

    private void ensureInactivateSegment(Integer lineId, Integer beforeId, Integer afterId) {
        int updated = segmentRepository
                .inactivateActiveSegment(lineId, beforeId, afterId);
        if (updated == 0) {
            throw CustomException.app(AppErrorCode.SEGMENT_NOT_FOUND)
                    .addParam("line id", lineId)
                    .addParam("before id", beforeId)
                    .addParam("after id", afterId);
        }
    }

    private Position resolveRole(Integer beforeId, Integer afterId) {
        if (beforeId == null && afterId != null) return Position.HEAD;
        if (beforeId != null && afterId ==null) return Position.TAIL;
        if (beforeId != null && afterId != null) return Position.INTERNAL;
        throw CustomException.app(AppErrorCode.SEGMENT_INPUT_VALUE_ERROR,
                "beforeId/afterId 조합이 올바르지 않습니다.");
    }

    private StationRoleInLine actualRole(Integer lineId, Integer stationId) {
        return segmentRepository.findActiveRole(lineId, stationId);
    }

    private void ensureRole(Integer lineId, Integer stationId, Position required,
                            StationRoleInLine actual) {
        if (actual == StationRoleInLine.NOT_IN_LINE) {
            throwExceptionByLineIdAndStationId(AppErrorCode.STATION_NOT_EXISTS_IN_LINE, lineId, stationId);
        }
        boolean ok = switch (required) {
            case HEAD -> actual == StationRoleInLine.HEAD;
            case TAIL -> actual == StationRoleInLine.TAIL;
            case INTERNAL -> actual == StationRoleInLine.INTERNAL;
        };
        if (!ok) {
            throwExceptionByLineIdAndStationId(AppErrorCode.SEGMENT_INPUT_VALUE_ERROR, lineId, stationId);
        }
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
            throw CustomException.domain(DomainErrorCode.STATION_NOT_FOUND)
                    .addParam("id", id);
        }
    }

    private void throwExceptionByLineIdAndStationId(ErrorCode errorCode, Integer lineId, Integer stationId) {
        throw CustomException.app(errorCode)
                .addParam("line id", lineId)
                .addParam("station id", stationId);
    }

}
