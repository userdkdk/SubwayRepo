package com.example.app.common.response;

import com.example.core.common.exception.ErrorCode;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

@Getter
public class CustomResponse<T> {

    private final boolean success;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final T data;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final ErrorResponse error;
    private final LocalDateTime timestamp;

    private CustomResponse(boolean success, T data, ErrorResponse error) {
        this.success = success;
        this.data = data;
        this.error = error;
        this.timestamp = LocalDateTime.now();
    }
    
    public static <T> ResponseEntity<CustomResponse<T>> ok() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new CustomResponse<>(true, null, null));
    }
    
    public static <T> ResponseEntity<CustomResponse<T>> ok(T data) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new CustomResponse<>(true, data, null));
    }
    
    public static <T> ResponseEntity<CustomResponse<T>> create() {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new CustomResponse<>(true, null, null));
    }

    public static <T> ResponseEntity<CustomResponse<T>> error(ErrorCode errorCode) {
        return ResponseEntity
                .status(getStatus(errorCode))
                .body(new CustomResponse<>(false, null, new ErrorResponse(errorCode.code(),errorCode.message())));
    }

    public static <T> ResponseEntity<CustomResponse<T>> error(ErrorCode errorCode, String message) {
        return ResponseEntity
                .status(getStatus(errorCode))
                .body(new CustomResponse<>(false, null, new ErrorResponse(errorCode.code(),message)));
    }

    public static <T> ResponseEntity<CustomResponse<T>> error(ErrorCode errorCode, T data) {
        return ResponseEntity
                .status(getStatus(errorCode))
                .body(new CustomResponse<>(false, data, new ErrorResponse(errorCode.code(),errorCode.message())));
    }

    private static HttpStatus getStatus(ErrorCode errorCode) {
        return HttpStatus.valueOf(errorCode.status());
    }
}
