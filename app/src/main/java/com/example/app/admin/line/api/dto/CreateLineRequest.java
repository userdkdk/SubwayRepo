package com.example.app.admin.line.api.dto;

import lombok.Getter;

@Getter
public class CreateLineRequest {
    private String name;
    private Integer startId;
    private Integer endId;
}
