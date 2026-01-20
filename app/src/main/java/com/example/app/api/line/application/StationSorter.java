package com.example.app.api.line.application;

import com.example.app.business.segment.SegmentJpaEntity;
import com.example.app.common.exception.AppErrorCode;
import com.example.core.common.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@RequiredArgsConstructor
public class StationSorter {

    public List<SegmentJpaEntity> sortSegments(List<SegmentJpaEntity> segs) {
        if (segs.isEmpty()) return List.of();
        Map<Integer, SegmentJpaEntity> nextByBefore = new HashMap<>(segs.size() * 2);
        Map<Integer, Integer> inDegree = new HashMap<>(segs.size() * 2);
        Integer lineId = segs.get(0).getLineJpaEntity().getId();

        // get degree
        for (SegmentJpaEntity s : segs) {
            Integer beforeId = s.getBeforeStationJpaEntity().getId();
            Integer afterId = s.getAfterStationJpaEntity().getId();
            if (nextByBefore.put(beforeId, s) != null) {
                throw CustomException.app(AppErrorCode.INTERNAL_SERVER_ERROR,
                                "[SORT STATION] Before station duplicated. Check DB")
                        .addParam("Line id",lineId);
            }
            inDegree.merge(afterId, 1, Integer::sum);
            inDegree.putIfAbsent(beforeId, 0);
        }

        // get start
        Integer startStationId = null;
        for (var e : inDegree.entrySet()) {
            if (e.getValue()==0) {
                if (startStationId != null) {
                    throw CustomException.app(AppErrorCode.INTERNAL_SERVER_ERROR,
                            "[SORT STATION] Multiple start station")
                            .addParam("line id", lineId);
                }
                startStationId = e.getKey();
            }
        }

        if (startStationId==null) {
            throw CustomException.app(AppErrorCode.INTERNAL_SERVER_ERROR,
                    "[SORT STATION] Start Station not found")
                    .addParam("Line id", lineId);
        }
        List<SegmentJpaEntity> ordered = new ArrayList<>();
        Integer cur = startStationId;

        for (int i=0;i<segs.size();i++) {
            SegmentJpaEntity seg = nextByBefore.get(cur);
            if (seg==null) {
                throw CustomException.app(AppErrorCode.INTERNAL_SERVER_ERROR,
                        "[SORT STATION] Chain broken")
                        .addParam("Line id", lineId);
            }
            ordered.add(seg);
            cur = seg.getAfterStationJpaEntity().getId();
        }

        return ordered;
    }
}
