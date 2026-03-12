package com.example.core.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DomainErrorCode implements ErrorCode {
    NAME_ERROR("D-001", "Input Name is empty",400),
    NAME_POLICY_ERROR("D-002", "Name validator length error",400),
    // station
    STATION_NOT_FOUND("DS-001", "Station not found", 404),
    STATION_NAME_DUPLICATED("DS-002", "Station name duplicated", 409),
    STATION_ALREADY_EXISTS_IN_LINE("DS-003", "Station already exists in line", 400),
    STATION_DELETE_ERROR("DS-004", "Can not delete station", 400),
    STATION_NOT_ACTIVE("DS-005", "Station is not active", 400),

    // line
    LINE_NOT_FOUND("DL-001", "Line not found", 404),
    LINE_NAME_DUPLICATED("DL-002", "Line name duplicated", 409),
    LINE_INPUT_STATION_SAME("DL-003","Input stations are same", 400),
    LINE_STATUS_CONFLICT("DL-004", "Line status is not expected", 409),
    LINE_MINIMUM_STATION_VIOLATION("DL-005", "Line has at least 2 stations", 400),

    // segment
    SEGMENT_NOT_FOUND("DSG-001", "Segment not found", 404),
    SEGMENT_INPUT_STATION_SAME("DSG-002", "Input stations are same", 400),
    SEGMENT_ALREADY_EXISTS("DSG-003", "Segment already exists", 409),
    SEGMENT_ATTRIBUTE_VALUE_ERROR("DSG-004", "Input Segment value error", 400),
    STATION_NOT_EXISTS_IN_LINE("DSG-005", "Station not exists in line", 400);

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
