package com.promocodes.promocodesmanager.exception;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public class ApiExceptionDto {
    private String message;
    private LocalDateTime timestamp;
    private HttpStatus httpStatus;
    private String debugMessage;

    public ApiExceptionDto() {
    }

    public ApiExceptionDto(String message,
                           LocalDateTime timestamp,
                           HttpStatus httpStatus,
                           String debugMessage) {
        this.message = message;
        this.timestamp = timestamp;
        this.httpStatus = httpStatus;
        this.debugMessage = debugMessage;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    public String getDebugMessage() {
        return debugMessage;
    }

    public void setDebugMessage(String debugMessage) {
        this.debugMessage = debugMessage;
    }
}
