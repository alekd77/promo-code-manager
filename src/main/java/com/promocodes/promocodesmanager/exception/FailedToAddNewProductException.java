package com.promocodes.promocodesmanager.exception;

import org.springframework.http.HttpStatus;

public class FailedToAddNewProductException extends ApiException {
    public FailedToAddNewProductException(HttpStatus httpStatus, String debugMessage) {
        super("Failed to add new product.", httpStatus, debugMessage);
    }
}
