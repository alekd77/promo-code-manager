package com.promocodes.promocodesmanager.exception;

import org.springframework.http.HttpStatus;

public class PurchaseException extends ApiException {
    public PurchaseException(HttpStatus status, String debugMessage) {
        super("Purchase failed.", status, debugMessage);
    }
}
