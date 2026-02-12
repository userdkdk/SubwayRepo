package com.example.app.api.station.api.dto.response;

import com.example.core.common.domain.enums.ActiveType;

import java.util.List;

public record StationDetailResponse(
    Integer id,
    String name,
    ActiveType activeType,
    List<LineItem> items
) {
    public record LineItem(
            Integer lineId,
            List<SegmentItem> segments) {}
    public record SegmentItem(Integer segmentId, ActiveType activeType) {}
}
