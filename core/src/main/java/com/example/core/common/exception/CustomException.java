package com.example.core.common.exception;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public class CustomException extends RuntimeException{

    private final ErrorCode errorCode;
    private final ErrorType errorType;
    private final Map<String, Object> params = new HashMap<>();

    public static CustomException domain(ErrorCode code) {
        return new CustomException(code, ErrorType.DOMAIN, null);
    }

    public static CustomException domain(ErrorCode code, String message) {
        return new CustomException(code, ErrorType.DOMAIN, message);
    }

    public static CustomException app(ErrorCode code) {
        return new CustomException(code, ErrorType.APPLICATION, null);
    }

    public static CustomException app(ErrorCode code, String message) {
        return new CustomException(code, ErrorType.APPLICATION, message);
    }

    private CustomException(ErrorCode code, ErrorType type, String message) {
        super(message != null ? message : code.message());
        this.errorCode = code;
        this.errorType = type;
    }

    public CustomException addParam(String paramName, Object paramValue) {
        this.params.put(paramName, paramValue);
        return this;
    }
}
