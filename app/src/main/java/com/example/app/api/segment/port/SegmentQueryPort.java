package com.example.app.api.segment.port;

import com.example.app.api.line.port.row.LineSegmentRow;
import com.example.core.domain.path.port.data.PathSegmentData;
import com.example.app.api.station.port.row.StationSegmentRow;
import com.example.app.common.dto.request.enums.StatusFilter;

import java.util.List;

public interface SegmentQueryPort {
    List<LineSegmentRow> findByLineAndActiveType(Integer lineId, StatusFilter status);
    List<PathSegmentData> findAllActive();
    List<StationSegmentRow> findByStationId(Integer stationId);
}
