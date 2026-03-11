package com.example.core.common.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ActiveType {
    ACTIVE("Active"),
    INACTIVE("Inactive");

    private final String status;
}
