package com.example.app.api.station.adapter;

import com.example.app.api.station.api.dto.response.StationResponse;
import com.example.app.api.station.api.dto.response.StationSegmentResponse;
import com.example.app.business.segment.SegmentJpaEntity;
import com.example.app.business.station.StationJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class StationApiMapper {

    public StationResponse entityToDto(StationJpaEntity entity) {
        return StationResponse.builder()
                .name(entity.getName())
                .activeType(entity.getActiveType())
                .build();
    }

    public StationSegmentResponse segmentEntityToDto(SegmentJpaEntity entity) {
        return StationSegmentResponse.builder()
                .beforeId(entity.getBeforeStationJpaEntity().getId())
                .beforeName(entity.getBeforeStationJpaEntity().getName())
                .afterId(entity.getAfterStationJpaEntity().getId())
                .afterName(entity.getAfterStationJpaEntity().getName())
                .distance(entity.getDistance())
                .spendTime(entity.getSpendTime())
                .build();
    }
}
