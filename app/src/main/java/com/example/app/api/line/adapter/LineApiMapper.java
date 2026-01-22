package com.example.app.api.line.adapter;

import com.example.app.api.line.api.dto.response.LineResponse;
import com.example.app.business.line.LineJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class LineApiMapper {
    public LineResponse entityToDto(LineJpaEntity lineJpaEntity) {
        return LineResponse.builder()
                .id(lineJpaEntity.getId())
                .name(lineJpaEntity.getName())
                .build();
    }
}
