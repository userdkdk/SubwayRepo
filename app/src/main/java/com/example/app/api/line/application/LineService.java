package com.example.app.api.line.application;

import com.example.app.api.line.api.dto.request.line.CreateLineRequest;
import com.example.app.api.line.api.dto.request.line.UpdateLineAttributeRequest;
import com.example.app.api.line.api.dto.request.line.UpdateLineStatusRequest;
import com.example.app.common.dto.request.enums.ActionType;
import com.example.app.common.exception.AppErrorCode;
import com.example.core.business.line.Line;
import com.example.core.business.line.LineName;
import com.example.core.business.line.LineRepository;
import com.example.core.business.lineSnapshot.LineSnapshotRepository;
import com.example.core.business.lineSnapshot.LineSnapshotSegment;
import com.example.core.business.lineSnapshot.LineSnapshotSegmentRepository;
import com.example.core.business.segment.Segment;
import com.example.core.business.segment.SegmentAttribute;
import com.example.core.business.segment.SegmentRepository;
import com.example.core.business.segmentHistory.SegmentHistory;
import com.example.core.business.segmentHistory.SegmentHistoryRepository;
import com.example.core.business.station.StationRepository;
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
        // check station exists and isActive
        checkStationExists(startId);
        checkStationExists(endId);
        // create line
        LineName name = new LineName(request.name());
        Line savedLine = lineRepository.save(Line.create(name));

        // upsert segment
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
        int updated = lineRepository.updateStatus(id,from, target);
        if (updated==0) {
            return;
        }
        if (target == ActiveType.ACTIVE) {
            // snapshot 조회
            List<Integer> segIds = lineSnapshotRepository.findSegsIdByLine(id);

            // snapshot에있는요소들전부활성화
            int segUpdated = segmentRepository.activateByIdAndLineId(id,segIds);
            if (segUpdated!=segIds.size()) {
                throw CustomException.app(AppErrorCode.SNAPSHOT_COUNT_CONFLICT)
                        .addParam("line id", id)
                        .addParam("segments counts", segIds.size())
                        .addParam("update counts", segUpdated);
            }
            return;
        }
        // deactivate
        Integer snapshotId = lineSnapshotRepository.save(id);
        lineSnapshotSegmentRepository.insertAllByLineId(snapshotId, id);
        segmentRepository.deactivateAllByLineId(id);
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
            throw CustomException.domain(AppErrorCode.STATION_NOT_FOUND)
                    .addParam("id", id);
        }
    }
}
