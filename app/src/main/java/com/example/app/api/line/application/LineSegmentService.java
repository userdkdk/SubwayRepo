package com.example.app.api.line.application;

import com.example.app.api.line.api.dto.request.segment.CreateSegmentRequest;
import com.example.app.common.exception.AppErrorCode;
import com.example.app.common.redis.event.LineSegmentChangedEvent;
import com.example.core.common.domain.enums.ActiveType;
import com.example.core.common.exception.DomainErrorCode;
import com.example.core.domain.line.Line;
import com.example.core.domain.station.StationConnectionInfo;
import com.example.core.domain.line.LineRepository;
import com.example.core.domain.segment.Segment;
import com.example.core.domain.segment.SegmentAttribute;
import com.example.core.domain.segment.SegmentRepository;
import com.example.core.domain.segmentHistory.SegmentHistory;
import com.example.core.domain.segmentHistory.SegmentHistoryRepository;
import com.example.core.domain.station.StationRepository;
import com.example.core.domain.station.StationRoleInLine;
import com.example.core.common.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

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

        // line lock and check active
        Line line = lineRepository.findByIdForUpdate(lineId);
        line.isActive();

        // station lock
        lockStations(stationId, beforeId, afterId);

        // check station does not exist in line
        if (segmentRepository.existsActiveSegmentByStationAndLine(lineId, stationId)) {
            throw CustomException.app(DomainErrorCode.STATION_ALREADY_EXISTS_IN_LINE)
                    .addParam("line id", lineId)
                    .addParam("station id", stationId);
        }


        // find station role
        StationRoleInLine inputRole = resolveRole(beforeId, afterId);
        switch (inputRole) {
            case HEAD -> {
                // check after is head
                StationRoleInLine headSegment = segmentRepository.findActiveRole(lineId, afterId);
                ensureRole(lineId, afterId, inputRole, headSegment);
                // save
                upsertSegment(lineId, stationId, afterId, afterDistance, afterSpendTime);
            }
            case TAIL -> {
                // check before is tail
                StationRoleInLine tailSegment = segmentRepository.findActiveRole(lineId, beforeId);
                ensureRole(lineId, beforeId, inputRole, tailSegment);
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
    public void removeStationInLine(Integer lineId, Integer stationId) {
        // line lock and check is active
        Line line = lineRepository.findByIdForUpdate(lineId);
        line.isActive();

        // station lock
        stationRepository.findByIdForUpdate(stationId);

        int activeSegmentCount = segmentRepository.countActiveByLine(lineId);
        if (activeSegmentCount<=1) {
            throw CustomException.domain(DomainErrorCode.LINE_MINIMUM_STATION_VIOLATION);
        }

        StationConnectionInfo info = segmentRepository.findRemovableInfo(lineId, stationId);

        switch (info.role()) {
            case HEAD -> {
                // inactive head
                ensureInactivateSegment(lineId,stationId,info.afterStationId());
            }
            case TAIL -> {
                // inactive tail
                ensureInactivateSegment(lineId, info.beforeStationId(), stationId);
            }
            case INTERNAL -> {
                // inactivate before and after
                ensureInactivateSegment(lineId, info.beforeStationId(), stationId);
                ensureInactivateSegment(lineId, stationId, info.afterStationId());

                // update or save
                double mergeDistance = info.beforeDistance() + info.afterDistance();
                int mergeSpendTime = info.beforeSpendTime() + info.afterSpendTime();
                upsertSegment(lineId, info.beforeStationId(), info.afterStationId(),
                        mergeDistance, mergeSpendTime);
            }
            case NOT_IN_LINE -> {
                throw CustomException.app(AppErrorCode.STATION_NOT_EXISTS_IN_LINE,
                        "해당 역은 이 노선에 존재하지 않습니다.");
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

    private StationRoleInLine resolveRole(Integer beforeId, Integer afterId) {
        if (beforeId == null && afterId != null) return StationRoleInLine.HEAD;
        if (beforeId != null && afterId ==null) return StationRoleInLine.TAIL;
        if (beforeId != null && afterId != null) return StationRoleInLine.INTERNAL;
        throw CustomException.app(AppErrorCode.SEGMENT_INPUT_VALUE_ERROR,
                "beforeId/afterId 조합이 올바르지 않습니다.");
    }

    private void ensureRole(Integer lineId, Integer stationId, StationRoleInLine inputRole,
                            StationRoleInLine segmentRole) {
        if (inputRole!=segmentRole || inputRole==StationRoleInLine.NOT_IN_LINE) {
            throw CustomException.app(AppErrorCode.SEGMENT_INPUT_VALUE_ERROR)
                    .addParam("line id", lineId)
                    .addParam("station id", stationId)
                    .addParam("input role", inputRole)
                    .addParam("segment role", segmentRole);
        }
    }

    private void upsertSegment(Integer id, Integer startId, Integer endId, Double distance, Integer spendTime) {
        SegmentAttribute segmentAttribute = new SegmentAttribute(distance, spendTime);
        Segment segment = Segment.create(id, startId, endId, segmentAttribute);
        segmentRepository.upsert(segment);
        Integer segmentId = segmentRepository.findIdByUniqueKey(segment);
        segmentHistoryRepository.save(SegmentHistory.create(segmentId));
    }

    private void lockStations(Integer... ids) {
        List<Integer> stationIds = Stream.of(ids)
                .filter(Objects::nonNull)
                .distinct()
                .sorted()
                .toList();
        stationRepository.findAllByIdsForUpdate(stationIds);
    }

}
