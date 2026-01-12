package com.example.core.domain.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ActiveType {
    ACTIVE("Active"),
    INACTIVE("Inactive");

    private final String status;
}
