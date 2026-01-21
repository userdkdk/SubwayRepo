package com.example.app.business.segment;

import com.example.app.common.exception.AppErrorCode;
import com.example.core.business.segment.Segment;
import com.example.core.business.station.Station;
import com.example.core.common.domain.enums.ActiveType;
import com.example.core.common.exception.CustomException;
import com.example.core.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class SegmentQueryRepository {
    private final SpringDataSegmentJpaRepository segmentJpaRepository;

    public List<SegmentJpaEntity> findByLine(Integer lineId) {
        return segmentJpaRepository.findByLineJpaEntity_IdAndActiveType(
                lineId, ActiveType.ACTIVE
        );
    }

    public void ensureStationNotInLine(Integer lineId, Integer stationId) {
        Optional<SegmentJpaEntity> seg = segmentJpaRepository.
                findByLineJpaEntity_idAndBeforeStationJpaEntity_idAndActiveType(
                lineId, stationId, ActiveType.ACTIVE
        );
        if (seg.isPresent()) {
            throwExceptionByLineIdAndStationId(AppErrorCode.SEGMENT_ALREADY_EXISTS, lineId, stationId);
        }
        seg = segmentJpaRepository.
                findByLineJpaEntity_idAndAfterStationJpaEntity_idAndActiveType(
                lineId, stationId, ActiveType.ACTIVE
        );
        if (seg.isPresent()) {
            throwExceptionByLineIdAndStationId(AppErrorCode.SEGMENT_ALREADY_EXISTS, lineId, stationId);
        }
    }

    public void ensureIsHead(Integer lineId, Integer endId) {
        Optional<SegmentJpaEntity> seg = segmentJpaRepository.
                findByLineJpaEntity_idAndBeforeStationJpaEntity_idAndActiveType(
                lineId, endId, ActiveType.ACTIVE
        );
        if (seg.isEmpty()) {
            throwExceptionByLineIdAndStationId(AppErrorCode.SEGMENT_NOT_FOUND, lineId, endId);
        }
        seg = segmentJpaRepository.
                findByLineJpaEntity_idAndAfterStationJpaEntity_idAndActiveType(
                lineId, endId, ActiveType.ACTIVE
        );
        if (seg.isPresent()) {
            throwExceptionByLineIdAndStationId(AppErrorCode.SEGMENT_ALREADY_EXISTS, lineId, endId);
        }
    }

    public void ensureIsTail(Integer lineId, Integer startId) {
        Optional<SegmentJpaEntity> seg = segmentJpaRepository.
                findByLineJpaEntity_idAndBeforeStationJpaEntity_idAndActiveType(
                        lineId, startId, ActiveType.ACTIVE
                );
        if (seg.isPresent()) {
            throwExceptionByLineIdAndStationId(AppErrorCode.SEGMENT_ALREADY_EXISTS, lineId, startId);
        }
        seg = segmentJpaRepository.
                findByLineJpaEntity_idAndAfterStationJpaEntity_idAndActiveType(
                        lineId, startId, ActiveType.ACTIVE
                );
        if (seg.isEmpty()) {
            throwExceptionByLineIdAndStationId(AppErrorCode.SEGMENT_NOT_FOUND, lineId, startId);
        }
    }

    public Optional<SegmentJpaEntity> findActiveByLineAndStation(Integer lineId, Integer startId, Integer endId) {
        return segmentJpaRepository
                .findByLineJpaEntity_idAndBeforeStationJpaEntity_idAndAfterStationJpaEntity_idAndActiveType(
                        lineId, startId, endId, ActiveType.ACTIVE
                );
    }

    private void throwExceptionByLineIdAndStationId(ErrorCode errorCode, Integer lineId, Integer stationId) {
        throw CustomException.app(errorCode)
                .addParam("line id", lineId)
                .addParam("station id", stationId);
    }
}
