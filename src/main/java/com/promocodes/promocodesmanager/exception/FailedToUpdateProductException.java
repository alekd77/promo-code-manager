package com.promocodes.promocodesmanager.exception;

import org.springframework.http.HttpStatus;

public class FailedToUpdateProductException extends ApiException {
    public FailedToUpdateProductException(HttpStatus httpStatus, String debugMessage) {
        super("Failed to update product.", httpStatus, debugMessage);
    }
}
