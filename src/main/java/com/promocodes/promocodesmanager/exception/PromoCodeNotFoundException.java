package com.promocodes.promocodesmanager.exception;

import org.springframework.http.HttpStatus;

public class PromoCodeNotFoundException extends ApiException {
    public PromoCodeNotFoundException() {
        super("Promo code not found.", HttpStatus.NOT_FOUND, "");
    }
}
