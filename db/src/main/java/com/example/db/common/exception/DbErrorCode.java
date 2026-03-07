package com.example.db.common.exception;

import com.example.core.common.exception.ErrorCode;
import com.example.core.common.exception.ErrorType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DbErrorCode implements ErrorCode {

    // error
    DATA_ERROR("D-001","DB ERROR", 500),
    INVALID_TEST_DB("D-002", "trying db is not test db", 409);


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
