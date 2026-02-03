package com.example.core.exception;

public interface ErrorCode {
    String code();
    String message();
    ErrorType errorType();
    int status();

}
