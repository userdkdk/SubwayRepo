package com.example.app.business.segment;

import com.example.app.business.line.LineJpaEntity;
import com.example.app.business.line.SpringDataLineJpaRepository;
import com.example.app.business.station.SpringDataStationJpaRepository;
import com.example.app.business.station.StationJpaEntity;
import com.example.app.common.exception.AppErrorCode;
import com.example.core.business.segment.Segment;
import com.example.core.business.segment.SegmentRepository;
import com.example.core.business.station.StationRoleInLine;
import com.example.core.common.domain.enums.ActiveType;
import com.example.core.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@Component
@RequiredArgsConstructor
public class SegmentRepositoryAdapter implements SegmentRepository {
    private final SpringDataSegmentJpaRepository segmentJpaRepository;
    private final SpringDataLineJpaRepository lineJpaRepository;
    private final SpringDataStationJpaRepository stationJpaRepository;
    private final SegmentMapper segmentMapper;

    @Override
    public Integer save(Segment segment) {
        Integer lineId = segment.getLineId();
        Integer beforeId = segment.getBeforeStationId();
        Integer afterId = segment.getAfterStationId();

        try {
            LineJpaEntity lineRef = lineJpaRepository.getReferenceById(lineId);
            StationJpaEntity beforeRef = stationJpaRepository.getReferenceById(beforeId);
            StationJpaEntity afterRef = stationJpaRepository.getReferenceById(afterId);

            SegmentJpaEntity entity = segmentMapper.toNewEntity(segment, lineRef, beforeRef, afterRef);
            SegmentJpaEntity saved = segmentJpaRepository.save(entity);
            return saved.getId();
        } catch (DataIntegrityViolationException e) {

            throw CustomException.domain(AppErrorCode.SEGMENT_ALREADY_EXISTS)
                    .addParam("lineId", lineId)
                    .addParam("beforeStationId", beforeId)
                    .addParam("afterStationId", afterId);
        }
    }

    @Override
    public void update(Integer id, Consumer<Segment> updater) {

    }

    @Override
    public boolean existsActiveStationInLine(Integer lineId, Integer stationId) {
        return segmentJpaRepository.existsActiveStationInLine(lineId, stationId, ActiveType.ACTIVE);
    }

    @Override
    public boolean existsActiveStation(Integer stationId) {
        return segmentJpaRepository.existsActiveStation(stationId, ActiveType.ACTIVE);
    }

    @Override
    public StationRoleInLine findActiveRole(Integer lineId, Integer stationId) {
        boolean asBefore = segmentJpaRepository.existsByLineJpaEntity_IdAndBeforeStationJpaEntity_IdAndActiveType(lineId, stationId, ActiveType.ACTIVE);
        boolean asAfter  = segmentJpaRepository.existsByLineJpaEntity_IdAndAfterStationJpaEntity_IdAndActiveType(lineId, stationId, ActiveType.ACTIVE);

        if (asBefore && asAfter) {
            return StationRoleInLine.INTERNAL;
        }
        if (asBefore) {
            return StationRoleInLine.HEAD;
        }
        if (asAfter) {
            return StationRoleInLine.TAIL;
        }
        return StationRoleInLine.NOT_IN_LINE;
    }

    @Override
    public int inactivateActiveSegment(Integer lineId, Integer beforeId, Integer afterId) {
        return segmentJpaRepository.inactivateActivateSegment(lineId, beforeId, afterId, ActiveType.INACTIVE, ActiveType.ACTIVE);
    }

}
