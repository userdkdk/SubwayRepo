package com.example.core.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DomainErrorCode implements ErrorCode {
    NAME_ERROR("D-001", "Input Name Invalid",400),
    NAME_POLICY_ERROR("D-002", "Input Policy Invalid",400),
    // station
    STATION_NAME_ERROR("DS-001", "Station name invalid", 400),
    STATION_DELETE_ERROR("DS-002", "Can not delete station", 400),

    // line
    LINE_NAME_ERROR("DL-001", "Line name invalid", 400),
    LINE_INPUT_STATION_SAME("DL-002","Input stations are same", 400),

    // segment
    SEGMENT_INPUT_STATION_SAME("DSG-001", "Input stations are same", 400),
    SEGMENT_INPUT_VALUE_ERROR("DSG-002", "Input Segment value error", 400);

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
