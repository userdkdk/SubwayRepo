package com.example.core.domain.segment;

import com.example.core.domain.station.StationConnectionInfo;
import com.example.core.domain.station.StationRoleInLine;

import java.util.List;
import java.util.function.Consumer;

public interface SegmentRepository{
    int upsert(Segment segment);
    void updateAttribute(Integer id, SegmentAttribute attribute);
    Integer findIdByUniqueKey(Segment segment);
    boolean existsActiveSegmentByStationAndLine(Integer lineId, Integer stationId);
    boolean existsActiveSegmentByStation(Integer stationId);

    StationRoleInLine findActiveRole(Integer lineId, Integer stationId);
    int inactivateActiveSegment(Integer lineId, Integer beforeId, Integer afterId);

    int activateByIds(List<Integer> segIds);

    int deactivateAllBySnapshotId(Integer snapshotId);

    StationConnectionInfo findRemovableInfo(Integer lineId, Integer stationId);

    int countActiveByLine(Integer lineId);
}
