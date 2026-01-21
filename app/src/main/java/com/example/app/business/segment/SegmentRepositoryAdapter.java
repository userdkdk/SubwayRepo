package com.example.app.business.segment;

import com.example.app.business.line.LineJpaEntity;
import com.example.app.business.line.SpringDataLineJpaRepository;
import com.example.app.business.station.SpringDataStationJpaRepository;
import com.example.app.business.station.StationJpaEntity;
import com.example.app.common.exception.AppErrorCode;
import com.example.core.business.segment.Segment;
import com.example.core.business.segment.SegmentRepository;
import com.example.core.common.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SegmentRepositoryAdapter implements SegmentRepository {
    private final SpringDataSegmentJpaRepository segmentJpaRepository;
    private final SpringDataLineJpaRepository lineJpaRepository;
    private final SpringDataStationJpaRepository stationJpaRepository;
    private final SegmentMapper segmentMapper;

    @Override
    public void save(Segment segment) {
        Integer lineId = segment.getLineId();
        Integer beforeId = segment.getBeforeStationId();
        Integer afterId = segment.getAfterStationId();

        try {
            LineJpaEntity lineRef = lineJpaRepository.getReferenceById(lineId);
            StationJpaEntity beforeRef = stationJpaRepository.getReferenceById(beforeId);
            StationJpaEntity afterRef = stationJpaRepository.getReferenceById(afterId);

            SegmentJpaEntity entity = segmentMapper.toNewEntity(segment, lineRef, beforeRef, afterRef);
            segmentJpaRepository.save(entity);
        } catch (DataIntegrityViolationException e) {
            throw CustomException.domain(AppErrorCode.SEGMENT_ALREADY_EXISTS)
                    .addParam("lineId", lineId)
                    .addParam("beforeStationId", beforeId)
                    .addParam("afterStationId", afterId);
        }
    }
}
