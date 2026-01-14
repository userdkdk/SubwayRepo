package com.example.core.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DomainErrorCode implements ErrorCode {
    NAME_ERROR("D-001", "Input Name Invalid"),
    POLICY_ERROR("D-002", "Input Policy Invalid"),
    // station
    STATION_NAME_ERROR("DS-001", "Station name invalid"),
    STATION_NAME_DUPLICATED("DS-002", "Station name duplicated"),

    // line
    LINE_NAME_ERROR("DL-001", "Line name invalid"),
    LINE_NAME_DUPLICATED("DL-002", "Line name duplicated");

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

    @Override
    public ErrorType errorType() {
        return ErrorType.DOMAIN;
    }

    @Override
    public int status() {
        return 400;
    }
}
