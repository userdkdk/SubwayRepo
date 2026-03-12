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
    public void updateLineName(Integer id, UpdateLineAttributeRequest request) {
        LineName name = new LineName(request.name());
        lineRepository.updateName(id, name);
    }

    @Transactional
    public void updateLineStatus(Integer id, UpdateLineStatusRequest request) {
        ActiveType target = request.actionType().toActiveType();

        // 라인 비관락 걸기
        Line line = lineRepository.ensureExistsForUpdate(id);

        // line 상태 검증
        line.ensureChangeActiveType(target);

        if (target == ActiveType.ACTIVE) {
            // find segment ids and lock stations
            List<Integer> segmentIds = lineSnapshotRepository.findSegsIdByLine(id);
            List<Integer> stationIds = segmentRepository.findStationIdsBySegments(segmentIds);
            stationRepository.findAllByIdsForUpdate(stationIds);

            // snapshot에있는요소들전부활성화
            int segUpdated = segmentRepository.activateAllByIds(segmentIds);
            if (segUpdated!=segmentIds.size()) {
                throw CustomException.app(AppErrorCode.SNAPSHOT_COUNT_CONFLICT)
                        .addParam("line id", id)
                        .addParam("segments counts", segmentIds.size())
                        .addParam("update counts", segUpdated);
            }
            // 라인 업데이트
            lineRepository.updateStatus(id, target);
            return;
        }
        // deactivate
        // find segment ids and lock stations
        List<Integer> segmentIds = segmentRepository.findActiveSegmentIdsByLine(id);
        List<Integer> stationIds = segmentRepository.findStationIdsBySegments(segmentIds);
        stationRepository.findAllByIdsForUpdate(stationIds);

        Integer snapshotId = lineSnapshotRepository.save(LineSnapshot.create(id));
        int snapshotInserts = lineSnapshotSegmentRepository.insertAll(snapshotId, segmentIds);
        int segmentCounts = segmentRepository.deactivateAllByIds(segmentIds);

        if (snapshotInserts != segmentCounts) {
            throw CustomException.app(AppErrorCode.SNAPSHOT_COUNT_CONFLICT)
                    .addParam("snapshot counts", snapshotInserts)
                    .addParam("segment counts", segmentCounts);
        }
        // 라인 업데이트
        lineRepository.updateStatus(id, target);
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
