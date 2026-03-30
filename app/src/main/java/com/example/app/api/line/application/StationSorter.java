package com.example.app.api.line.application;

import com.example.app.api.line.port.query.row.LineSegmentRow;
import com.example.app.common.exception.AppErrorCode;
import com.example.core.common.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@RequiredArgsConstructor
public class StationSorter {

    // active type도 같이 입력받게해서 경우따라 에러 반환하게하기
    public List<LineSegmentRow> sortSegments(List<LineSegmentRow> segs, Integer lineId) {
        if (segs.isEmpty()) return List.of();
        Map<Integer, LineSegmentRow> nextByBefore = new HashMap<>(segs.size() * 2);
        Map<Integer, Integer> inDegree = new HashMap<>(segs.size() * 2);

        // get degree
        for (LineSegmentRow s : segs) {
            Integer beforeId = s.beforeStationId();
            Integer afterId = s.afterStationId();
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
        List<LineSegmentRow> ordered = new ArrayList<>();
        Integer cur = startStationId;

        for (int i=0;i<segs.size();i++) {
            LineSegmentRow seg = nextByBefore.get(cur);
            if (seg==null) {
                throw CustomException.app(AppErrorCode.INTERNAL_SERVER_ERROR,
                        "[SORT STATION] Chain broken")
                        .addParam("Line id", lineId);
            }
            ordered.add(seg);
            cur = seg.afterStationId();
        }

        return ordered;
    }
}
