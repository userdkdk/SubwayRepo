package com.example.app.common.exception;

import com.example.core.common.exception.ErrorCode;
import com.example.core.common.exception.ErrorType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum AppErrorCode implements ErrorCode {
    INTERNAL_SERVER_ERROR("A-001","Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR),
    DATA_INTEGRITY_VIOLATION("A-002", "DB Save Error", HttpStatus.CONFLICT),
    // station
    STATION_NOT_FOUND("AS-001", "Station not found", HttpStatus.NOT_FOUND),

    // line
    LINE_NOT_FOUND("AL-001", "Line not found", HttpStatus.NOT_FOUND),

    // segment,
    SEGMENT_NOT_FOUND("ASG-001", "Segment not found", HttpStatus.NOT_FOUND),
    SEGMENT_ALREADY_EXISTS("ASG-002", "Segment already exists", HttpStatus.CONFLICT),
    SEGMENT_INPUT_VALUE_ERROR("ASG-003", "Segment input value error", HttpStatus.BAD_REQUEST);


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
