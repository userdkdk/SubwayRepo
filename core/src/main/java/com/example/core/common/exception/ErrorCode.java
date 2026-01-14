package com.example.core.common.exception;

public interface ErrorCode {
    String code();
    String message();
    ErrorType errorType();
    int status();

}
