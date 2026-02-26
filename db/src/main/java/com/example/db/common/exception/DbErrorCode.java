package com.example.db.common.exception;

import com.example.core.exception.ErrorCode;
import com.example.core.exception.ErrorType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DbErrorCode implements ErrorCode {

    // error
    DATA_ERROR("D-001","DB ERROR", 500),

    // station
    STATION_NOT_FOUND("AS-001", "Station not found", 404),
    STATION_NAME_DUPLICATED("AS-002", "Station name duplicated", 409),
    STATION_ALREADY_EXISTS_IN_LINE("AS-003", "Station already exists in line", 400),
    ACTIVE_STATION_EXISTS("AS-004", "Active segments exists in station", 409),

    // line
    LINE_NOT_FOUND("AL-001", "Line not found", 404),
    LINE_NAME_DUPLICATED("AL-002", "Line name duplicated", 409),
    ACTIVE_LINE_EXISTS("AL-004", "Active segments exists in line", 409),


    // segment,
    SEGMENT_NOT_FOUND("ASG-001", "Segment not found", 404),
    SEGMENT_ALREADY_EXISTS("ASG-002", "Segment already exists", 409),
    SEGMENT_INPUT_VALUE_ERROR("ASG-003", "Segment input value error", 400),
    STATION_NOT_EXISTS_IN_LINE("ASG-004", "Station not exists in line", 400);

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
