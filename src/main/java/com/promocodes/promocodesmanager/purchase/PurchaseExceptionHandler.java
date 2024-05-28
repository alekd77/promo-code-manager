package com.promocodes.promocodesmanager.purchase;

import com.promocodes.promocodesmanager.exception.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class PurchaseExceptionHandler {
    private final ExceptionMapper exceptionMapper;

    @Autowired
    public PurchaseExceptionHandler(ExceptionMapper exceptionMapper) {
        this.exceptionMapper = exceptionMapper;
    }

    @ExceptionHandler({ProductNotFoundException.class})
    public ResponseEntity<ApiExceptionDto> handleProductNotFoundException(
            ProductNotFoundException ex) {
        ApiExceptionDto exceptionDto = exceptionMapper.toApiExceptionDto(ex);
        HttpStatus httpStatus = ex.getStatus();

        return new ResponseEntity<>(exceptionDto, httpStatus);
    }

    @ExceptionHandler({PromoCodeNotFoundException.class})
    public ResponseEntity<ApiExceptionDto> handlePromoCodeNotFoundException(
            PromoCodeNotFoundException ex) {
        ApiExceptionDto exceptionDto = exceptionMapper.toApiExceptionDto(ex);
        HttpStatus httpStatus = ex.getStatus();

        return new ResponseEntity<>(exceptionDto, httpStatus);
    }

    @ExceptionHandler({PurchaseException.class})
    public ResponseEntity<ApiExceptionDto> handlePurchaseException(
            PurchaseException ex) {
        ApiExceptionDto exceptionDto = exceptionMapper.toApiExceptionDto(ex);
        HttpStatus httpStatus = ex.getStatus();

        return new ResponseEntity<>(exceptionDto, httpStatus);
    }
}
