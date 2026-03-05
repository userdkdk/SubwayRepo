package com.example.core.domain.segment;

import com.example.core.domain.station.StationRoleInLine;

import java.util.List;
import java.util.function.Consumer;

public interface SegmentRepository{
    int upsert(Segment segment);
    Integer findIdByUniqueKey(Segment segment);
    void update(Integer id, Consumer<Segment> updater);
    boolean existsActiveStationInLine(Integer lineId, Integer stationId);
    boolean existsInactiveStationInLine(Integer lineId, Integer stationId);
    boolean existsActiveSegmentByStation(Integer stationId);
    boolean existsActiveSegmentByLine(Integer lineId);

    StationRoleInLine findActiveRole(Integer lineId, Integer stationId);
    int inactivateActiveSegment(Integer lineId, Integer beforeId, Integer afterId);

    int activateByIds(List<Integer> segIds);

    int deactivateAllBySnapshotId(Integer snapshotId);
}
