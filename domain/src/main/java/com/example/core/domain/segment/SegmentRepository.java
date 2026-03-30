package com.example.core.domain.segment;

import com.example.core.domain.station.StationConnectionInfo;
import com.example.core.domain.station.StationRoleInLine;

import java.util.List;

public interface SegmentRepository{
    int upsert(Segment segment);
    Segment updateAttribute(Integer id, SegmentAttribute attribute);
    Integer findIdByUniqueKey(Segment segment);
    boolean existsActiveSegmentByStationAndLine(Integer lineId, Integer stationId);
    boolean existsActiveSegmentByStation(Integer stationId);

    StationRoleInLine findActiveRole(Integer lineId, Integer stationId);
    int inactivateActiveSegment(Integer lineId, Integer beforeId, Integer afterId);

    int activateAllByIds(List<Integer> segIds);

    int deactivateAllByIds(List<Integer> segmentIds);

    StationConnectionInfo findRemovableInfo(Integer lineId, Integer stationId);

    int countActiveByLine(Integer lineId);

    List<Integer> findActiveSegmentIdsByLine(Integer id);

    List<Integer> findStationIdsBySegments(List<Integer> segmentsId);
}
