package com.example.app.admin.station.adapter;

import com.example.app.admin.station.api.dto.response.StationResponse;
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
}
