package com.example.app.common.exception;

import com.example.core.exception.ErrorCode;
import com.example.core.exception.ErrorType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum AppErrorCode implements ErrorCode {
    INTERNAL_SERVER_ERROR("A-001","Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR),
    DATA_INTEGRITY_VIOLATION("A-002", "DB Save Error", HttpStatus.CONFLICT),
    INVALID_INPUT_ERROR("A-003", "Invalid input error", HttpStatus.BAD_REQUEST),
    REDIS_SERIALIZATION_ERROR("A-004", "Redis serialization failed", HttpStatus.INTERNAL_SERVER_ERROR),
    CONCURRENT_MODIFICATION("A-005", "Input data conflict error", HttpStatus.CONFLICT),

    // station
    STATION_NOT_FOUND("AS-001", "Station not found", HttpStatus.NOT_FOUND),
    STATION_NAME_DUPLICATED("AS-002", "Station name duplicated", HttpStatus.CONFLICT),
    STATION_ALREADY_EXISTS_IN_LINE("AS-003", "Station already exists in line", HttpStatus.BAD_REQUEST),
    ACTIVE_STATION_EXISTS("AS-004", "Active segments exists in station", HttpStatus.CONFLICT),

    // line
    LINE_NOT_FOUND("AL-001", "Line not found", HttpStatus.NOT_FOUND),
    LINE_NAME_DUPLICATED("AL-002", "Line name duplicated", HttpStatus.CONFLICT),
    ACTIVE_LINE_EXISTS("AL-004", "Active segments exists in line", HttpStatus.CONFLICT),


    // segment,
    SEGMENT_NOT_FOUND("ASG-001", "Segment not found", HttpStatus.NOT_FOUND),
    SEGMENT_ALREADY_EXISTS("ASG-002", "Segment already exists", HttpStatus.CONFLICT),
    SEGMENT_INPUT_VALUE_ERROR("ASG-003", "Segment input value error", HttpStatus.BAD_REQUEST),
    STATION_NOT_EXISTS_IN_LINE("ASG-004", "Station not exists in line", HttpStatus.BAD_REQUEST),

    // history
    HISTORY_CONDITION_INValid("AH-001", "History input condition invalid", HttpStatus.BAD_REQUEST);


    private final String code;
    private final String message;
    private final HttpStatus status;

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
        return ErrorType.APPLICATION;
    }

    @Override
    public int status() {
        return status.value();
    }
}
