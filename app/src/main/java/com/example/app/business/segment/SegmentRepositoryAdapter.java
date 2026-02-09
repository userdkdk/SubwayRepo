package com.example.app.business.segment;

import com.example.app.business.line.LineJpaEntity;
import com.example.app.business.line.SpringDataLineJpaRepository;
import com.example.app.business.segment.projection.RoleCount;
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

import java.util.Optional;
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

        Optional<SegmentJpaEntity> entityOpt =
                segmentJpaRepository.findByLineJpaEntity_IdAndBeforeStationJpaEntity_IdAndAfterStationJpaEntity_Id(
                        lineId, beforeId, afterId
                );

        if (entityOpt.isPresent()) {
            SegmentJpaEntity entity = entityOpt.get();
            entity.setActiveType(ActiveType.ACTIVE);
            entity.setAttribute(segment.getSegmentAttribute().distance(),
                    segment.getSegmentAttribute().spendTimeSeconds());
            return entity.getId();
        }

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
        SegmentJpaEntity entity = segmentJpaRepository.findById(id)
                .orElseThrow(()->CustomException.app(AppErrorCode.SEGMENT_NOT_FOUND)
                .addParam("id", id));
        Segment domain = segmentMapper.toDomain(entity);
        updater.accept(domain);

        entity.setAttribute(domain.getSegmentAttribute().distance(),
                domain.getSegmentAttribute().spendTimeSeconds());
        entity.setActiveType(domain.getActiveType());
    }

    @Override
    public boolean existsActiveStationInLine(Integer lineId, Integer stationId) {
        return segmentJpaRepository.existsStationByLineAndActiveType(lineId, stationId, ActiveType.ACTIVE);
    }

    @Override
    public boolean existsInactiveStationInLine(Integer lineId, Integer stationId) {
        return segmentJpaRepository.existsStationByLineAndActiveType(lineId, stationId, ActiveType.INACTIVE);
    }

    @Override
    public boolean existsActiveSegmentByStation(Integer stationId) {
        return segmentJpaRepository.existsActiveSegmentByStation(stationId, ActiveType.ACTIVE);
    }

    @Override
    public boolean existsActiveSegmentByLine(Integer lineId) {
        return segmentJpaRepository.existsActiveSegmentByLine(lineId, ActiveType.ACTIVE);
    }

    @Override
    public StationRoleInLine findActiveRole(Integer lineId, Integer stationId) {
        RoleCount c = segmentJpaRepository.countRole(lineId, stationId, ActiveType.ACTIVE);
        boolean asBefore = c.beforeCount() > 0;
        boolean asAfter  = c.afterCount()  > 0;

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
