package com.example.app.business.line.api.dto;

import lombok.Getter;

@Getter
public class CreateLineRequest {
    private String name;
    private String startStation;
    private String endStation;
}
