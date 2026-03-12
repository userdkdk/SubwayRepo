package com.example.app.api.line.application;

import com.example.app.api.line.api.dto.request.line.CreateLineRequest;
import com.example.app.api.line.api.dto.request.line.UpdateLineAttributeRequest;
import com.example.app.api.line.api.dto.request.line.UpdateLineStatusRequest;
import com.example.app.common.exception.AppErrorCode;
import com.example.core.common.exception.DomainErrorCode;
import com.example.core.domain.line.Line;
import com.example.core.domain.line.LineName;
import com.example.core.domain.line.LineRepository;
import com.example.core.domain.lineSnapshot.LineSnapshot;
import com.example.core.domain.lineSnapshot.LineSnapshotRepository;
import com.example.core.domain.lineSnapshot.LineSnapshotSegmentRepository;
import com.example.core.domain.segment.Segment;
import com.example.core.domain.segment.SegmentAttribute;
import com.example.core.domain.segment.SegmentRepository;
import com.example.core.domain.segmentHistory.SegmentHistory;
import com.example.core.domain.segmentHistory.SegmentHistoryRepository;
import com.example.core.domain.station.Station;
import com.example.core.domain.station.StationRepository;
import com.example.core.common.domain.enums.ActiveType;
import com.example.core.common.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LineService {

    private final LineRepository lineRepository;
    private final StationRepository stationRepository;
    private final SegmentRepository segmentRepository;
    private final SegmentHistoryRepository segmentHistoryRepository;
    private final LineSnapshotRepository lineSnapshotRepository;
    private final LineSnapshotSegmentRepository lineSnapshotSegmentRepository;

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void createLine(CreateLineRequest request) {
        Integer startId = request.startId();
        Integer endId = request.endId();
        Double distance = request.attribute().distance();
        Integer spendTime = request.attribute().spendTime();
        // station lock and check is active
        checkStationActive(startId);
        checkStationActive(endId);
        // create line
        LineName name = new LineName(request.name());
        Line savedLine = lineRepository.save(Line.create(name));

        // insert segment
        upsertSegment(savedLine.getId(), startId, endId, distance, spendTime);
    }


    @Transactional
    public void updateLineAttribute(Integer id, UpdateLineAttributeRequest request) {
        LineName name = new LineName(request.name());
        lineRepository.updateAttribute(id, name);
    }

    @Transactional
    public void updateLineStatus(Integer id, UpdateLineStatusRequest request) {
        ActiveType target = request.actionType().toActiveType();
        ActiveType from = (target == ActiveType.ACTIVE) ? ActiveType.INACTIVE : ActiveType.ACTIVE;

        // 라인 비관락 걸기
        Line line = lineRepository.ensureExistsForUpdate(id);
        if (line.getActiveType() != from) {
            throw CustomException.domain(DomainErrorCode.LINE_STATUS_CONFLICT);
        }

        // 라인 업데이트
        lineRepository.updateStatus(id, from, target);

        if (target == ActiveType.ACTIVE) {
            // snapshot 조회
            List<Integer> segIds = lineSnapshotRepository.findSegsIdByLine(id);

            // snapshot에있는요소들전부활성화
            int segUpdated = segmentRepository.activateByIds(segIds);
            if (segUpdated!=segIds.size()) {
                throw CustomException.app(AppErrorCode.SNAPSHOT_COUNT_CONFLICT)
                        .addParam("line id", id)
                        .addParam("segments counts", segIds.size())
                        .addParam("update counts", segUpdated);
            }
            return;
        }
        // deactivate
        Integer snapshotId = lineSnapshotRepository.save(LineSnapshot.create(id));
        int snapshotCounts = lineSnapshotSegmentRepository.insertAllByLineId(snapshotId, id);
        int segmentCounts = segmentRepository.deactivateAllBySnapshotId(snapshotId);
        if (snapshotCounts != segmentCounts) {
            throw CustomException.app(AppErrorCode.SNAPSHOT_COUNT_CONFLICT)
                    .addParam("snapshot counts", snapshotCounts)
                    .addParam("segment counts", segmentCounts);
        }
    }

    private void upsertSegment(Integer lineId, Integer startId, Integer endId, Double distance, Integer spendTime) {
        SegmentAttribute segmentAttribute = new SegmentAttribute(distance, spendTime);
        Segment segment = Segment.create(lineId, startId, endId, segmentAttribute);
        segmentRepository.upsert(segment);
        Integer segmentId = segmentRepository.findIdByUniqueKey(segment);
        segmentHistoryRepository.save(SegmentHistory.create(segmentId));
    }

    private void checkStationActive(Integer id) {
        Station station = stationRepository.findByIdForUpdate(id);
        if (!station.isActive()) {
            throw CustomException.domain(DomainErrorCode.STATION_NOT_ACTIVE)
                    .addParam("id", id);
        }
    }
}
