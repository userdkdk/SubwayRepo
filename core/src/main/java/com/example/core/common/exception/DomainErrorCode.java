package com.example.core.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DomainErrorCode implements ErrorCode {
    NAME_ERROR("D-001", "Input Name Invalid",400),
    POLICY_ERROR("D-002", "Input Policy Invalid",400),
    // station
    STATION_NAME_ERROR("DS-001", "Station name invalid", 400),
    STATION_NOT_FOUND("DS-001", "Station not found", 404),

    // line
    LINE_NAME_ERROR("DL-001", "Line name invalid", 400),
    LINE_NAME_FOUND("DL-001", "Line not found", 404),
    LINE_INPUT_STATION_SAME("DL-003","Input stations are same", 400);

    private final String code;
    private final String message;
    private final int status;

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
        return status;
    }
}
