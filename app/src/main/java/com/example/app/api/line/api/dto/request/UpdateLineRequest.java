package com.example.app.api.line.api.dto.request;

import com.example.core.common.domain.enums.ActiveType;
import lombok.Getter;

@Getter
public class UpdateLineRequest {
    private String name;
    private ActiveType activeType;
}
