package com.example.core.business.segment;

import com.example.core.business.station.StationRoleInLine;

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
}
