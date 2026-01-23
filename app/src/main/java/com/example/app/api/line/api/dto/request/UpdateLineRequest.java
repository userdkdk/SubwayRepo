package com.example.app.api.line.api.dto.request;

import com.example.app.common.response.enums.StatusFilter;
import lombok.Getter;

@Getter
public class UpdateLineRequest {
    private String name;
    private StatusFilter status;
}
