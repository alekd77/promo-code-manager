package com.promocodes.promocodesmanager.exception;

import org.springframework.http.HttpStatus;

public class FailedToAddNewPromoCodeException extends ApiException {
    public FailedToAddNewPromoCodeException(HttpStatus httpStatus, String debugMessage) {
        super("Failed to add new promo code.", httpStatus, debugMessage);
    }
}
