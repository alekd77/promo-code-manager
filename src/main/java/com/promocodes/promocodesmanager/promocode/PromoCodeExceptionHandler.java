package com.promocodes.promocodesmanager.promocode;

import com.promocodes.promocodesmanager.exception.ApiExceptionDto;
import com.promocodes.promocodesmanager.exception.ExceptionMapper;
import com.promocodes.promocodesmanager.exception.FailedToAddNewPromoCodeException;
import com.promocodes.promocodesmanager.exception.PromoCodeNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class PromoCodeExceptionHandler {
    private final ExceptionMapper exceptionMapper;

    @Autowired
    public PromoCodeExceptionHandler(ExceptionMapper exceptionMapper) {
        this.exceptionMapper = exceptionMapper;
    }

    @ExceptionHandler({HttpMessageNotReadableException.class})
    public ResponseEntity<ApiExceptionDto> handleNumberFormatException(HttpMessageNotReadableException ex) {
        ApiExceptionDto exceptionDto = new ApiExceptionDto(
                "Invalid input data format.",
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST,
                ex.getMessage()
        );
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

        return new ResponseEntity<>(exceptionDto, httpStatus);
    }

    @ExceptionHandler({PromoCodeNotFoundException.class})
    public ResponseEntity<ApiExceptionDto> handlePromoCodeNotFoundException(PromoCodeNotFoundException ex) {
        ApiExceptionDto exceptionDto = exceptionMapper.toApiExceptionDto(ex);
        HttpStatus httpStatus = ex.getStatus();

        return new ResponseEntity<>(exceptionDto, httpStatus);
    }

    @ExceptionHandler({FailedToAddNewPromoCodeException.class})
    public ResponseEntity<ApiExceptionDto> handleFailedToAddNewPromoCodeException(FailedToAddNewPromoCodeException ex) {
        ApiExceptionDto exceptionDto = exceptionMapper.toApiExceptionDto(ex);
        HttpStatus httpStatus = ex.getStatus();

        return new ResponseEntity<>(exceptionDto, httpStatus);
    }
}
