package com.example.db.business.segment;

import com.example.core.common.exception.DomainErrorCode;
import com.example.core.domain.segment.SegmentAttribute;
import com.example.core.domain.station.StationConnectionInfo;
import com.example.db.business.segment.projection.RoleCount;
import com.example.core.domain.segment.Segment;
import com.example.core.domain.segment.SegmentRepository;
import com.example.core.domain.station.StationRoleInLine;
import com.example.core.common.domain.enums.ActiveType;
import com.example.core.common.exception.CustomException;
import com.example.db.common.exception.DbErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

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
    public void updateAttribute(Integer id, SegmentAttribute attribute) {
        SegmentJpaEntity entity = segmentJpaRepository.findById(id)
                .orElseThrow(()->CustomException.app(DomainErrorCode.SEGMENT_NOT_FOUND)
                        .addParam("id", id));
        entity.changeAttribute(attribute.distance(), attribute.spendTimeSeconds());
    }

    @Override
    public Integer findIdByUniqueKey(Segment segment) {
        return segmentJpaRepository.findByLineJpaEntity_IdAndBeforeStationJpaEntity_IdAndAfterStationJpaEntity_Id(
                segment.getLineId(), segment.getBeforeStationId(), segment.getAfterStationId()
        ).orElseThrow(()->CustomException.app(DomainErrorCode.SEGMENT_NOT_FOUND)
                .addParam("line id", segment.getLineId())
                .addParam("before id", segment.getBeforeStationId())
                .addParam("after id", segment.getAfterStationId())).getId();
    }

    @Override
    public boolean existsActiveSegmentByStationAndLine(Integer lineId, Integer stationId) {
        return segmentJpaRepository.existsStationByLineAndActiveType(lineId, stationId, ActiveType.ACTIVE);
    }

    @Override
    public boolean existsActiveSegmentByStation(Integer stationId) {
        return segmentJpaRepository.existsActiveSegmentByStation(stationId, ActiveType.ACTIVE);
    }

    @Override
    public StationRoleInLine findActiveRole(Integer lineId, Integer stationId) {
        RoleCount c = segmentJpaRepository.countRole(lineId, stationId, ActiveType.ACTIVE);
        boolean asBefore = c.beforeCount() > 0;
        boolean asAfter  = c.afterCount()  > 0;

        return StationRoleInLine.from(asBefore, asAfter);
    }

    @Override
    public int inactivateActiveSegment(Integer lineId, Integer beforeId, Integer afterId) {
        return segmentJpaRepository.inactivateActivateSegment(lineId, beforeId, afterId, ActiveType.INACTIVE, ActiveType.ACTIVE);
    }

    @Override
    public int activateByIds(List<Integer> segIds) {
        return segmentJpaRepository.activateByIdAndLineId(segIds);
    }

    @Override
    public int deactivateAllBySnapshotId(Integer snapshotId) {
        return segmentJpaRepository.deactivateAllBySnapshotId(snapshotId);
    }

    @Override
    public StationConnectionInfo findRemovableInfo(Integer lineId, Integer stationId) {
        return segmentJpaRepository.findStationConnection(lineId, stationId)
                .orElseThrow(()->CustomException.domain(DbErrorCode.DATA_ERROR));
    }

    @Override
    public int countActiveByLine(Integer lineId) {
        return segmentJpaRepository.countByLineJpaEntity_Id(lineId);
    }

}
