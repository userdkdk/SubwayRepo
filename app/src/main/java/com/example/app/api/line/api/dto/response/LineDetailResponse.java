package com.example.app.api.line.api.dto.response;

import com.example.app.api.station.api.dto.response.StationSegmentResponse;
import com.example.core.common.domain.enums.ActiveType;

import java.util.List;

public record LineDetailResponse (
        Integer id,
        String name,
        ActiveType activeType,
        Integer totalStation,
        String head,
        String tail,
        List<StationSegmentResponse> items
) {
    public static LineDetailResponse of(
            Integer id,
            String name,
            ActiveType activeType,
            List<StationSegmentResponse> items
    ) {
        int totalCount = items.size();
        String head = totalCount > 0 ? items.get(1).beforeName() : null;
        String tail = totalCount > 0 ? items.get(totalCount - 2).afterName() : null;

        return new LineDetailResponse(
                id,
                name,
                activeType,
                totalCount,
                head,
                tail,
                items
        );
    }

}
