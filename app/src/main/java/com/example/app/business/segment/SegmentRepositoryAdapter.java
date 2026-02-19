package com.example.app.business.segment;

import com.example.app.business.segment.projection.RoleCount;
import com.example.app.common.exception.AppErrorCode;
import com.example.core.business.segment.Segment;
import com.example.core.business.segment.SegmentRepository;
import com.example.core.business.station.StationRoleInLine;
import com.example.core.common.domain.enums.ActiveType;
import com.example.core.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@Slf4j
@Component
@RequiredArgsConstructor
public class SegmentRepositoryAdapter implements SegmentRepository {
    private final SpringDataSegmentJpaRepository segmentJpaRepository;
    private final SegmentMapper segmentMapper;

    @Override
    public int upsert(Segment segment) {
        return segmentJpaRepository.upsertSegment(
                segment.getLineId(),
                segment.getBeforeStationId(),
                segment.getAfterStationId(),
                ActiveType.ACTIVE.name(),
                segment.getSegmentAttribute().distance(),
                segment.getSegmentAttribute().spendTimeSeconds()
        );
    }

    @Override
    public Integer findIdByUniqueKey(Segment segment) {
        return segmentJpaRepository.findByLineJpaEntity_IdAndBeforeStationJpaEntity_IdAndAfterStationJpaEntity_Id(
                segment.getLineId(), segment.getBeforeStationId(), segment.getAfterStationId()
        ).orElseThrow(()->CustomException.app(AppErrorCode.SEGMENT_NOT_FOUND)
                .addParam("line id", segment.getLineId())
                .addParam("before id", segment.getBeforeStationId())
                .addParam("after id", segment.getAfterStationId())).getId();
    }

    @Override
    public void update(Integer id, Consumer<Segment> updater) {
        SegmentJpaEntity entity = segmentJpaRepository.findById(id)
                .orElseThrow(()->CustomException.app(AppErrorCode.SEGMENT_NOT_FOUND)
                .addParam("id", id));
        Segment domain = segmentMapper.toDomain(entity);
        updater.accept(domain);

        entity.changeAttribute(domain.getSegmentAttribute().distance(),
                domain.getSegmentAttribute().spendTimeSeconds());
        entity.changeActiveType(domain.getActiveType());
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
        log.info("----check----");
        RoleCount c = segmentJpaRepository.countRole(lineId, stationId, ActiveType.ACTIVE);
        boolean asBefore = c.beforeCount() > 0;
        boolean asAfter  = c.afterCount()  > 0;
        log.info("before: {}, after: {}", asBefore, asAfter);

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
