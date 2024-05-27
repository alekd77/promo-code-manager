package com.promocodes.promocodesmanager.exception;

import org.springframework.http.HttpStatus;

public class ProductDiscountCalculationException extends ApiException {
    public ProductDiscountCalculationException(HttpStatus httpStatus,
                                               String debugMessage) {
        super("Product discount calculation failed.",
                httpStatus,
                debugMessage
        );
    }
}
