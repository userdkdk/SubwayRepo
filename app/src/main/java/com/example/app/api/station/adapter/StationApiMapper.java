package com.example.app.api.station.adapter;

import com.example.app.api.station.api.dto.response.StationResponse;
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

    public StationResponse segmentEntityToDto(SegmentJpaEntity entity) {
        return StationResponse.builder()
                .build();
    }
}
