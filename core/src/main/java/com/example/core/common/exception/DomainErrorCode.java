package com.example.core.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DomainErrorCode implements ErrorCode {
    SAMPLE_ERROR("D?-001","Sample Error"),
    NAME_ERROR("DC-001", "Input Name Invalid"),
    POLICY_ERROR("DC-002", "Input Policy Invalid"),
    // station
    STATION_NAME_ERROR("DS-001", "Station name invalid");


    private final String code;
    private final String message;

    @Override
    public String code() {
        return code;
    }

    @Override
    public String message() {
        return message;
    }
}
