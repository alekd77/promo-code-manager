package com.promocodes.promocodesmanager.product;

import com.promocodes.promocodesmanager.exception.ApiExceptionDto;
import com.promocodes.promocodesmanager.exception.ExceptionMapper;
import com.promocodes.promocodesmanager.exception.FailedToAddNewProductException;
import com.promocodes.promocodesmanager.exception.FailedToUpdateProductException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class ProductExceptionHandler {
    private final ExceptionMapper exceptionMapper;

    @Autowired
    public ProductExceptionHandler(ExceptionMapper exceptionMapper) {
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

    @ExceptionHandler({FailedToAddNewProductException.class})
    public ResponseEntity<ApiExceptionDto> handleFailedToAddNewProductException(FailedToAddNewProductException ex) {
        ApiExceptionDto exceptionDto = exceptionMapper.toApiExceptionDto(ex);
        HttpStatus httpStatus = ex.getStatus();

        return new ResponseEntity<>(exceptionDto, httpStatus);
    }

    @ExceptionHandler({FailedToUpdateProductException.class})
    public ResponseEntity<ApiExceptionDto> handleFailedToUpdateProductException(FailedToUpdateProductException ex) {
        ApiExceptionDto exceptionDto = exceptionMapper.toApiExceptionDto(ex);
        HttpStatus httpStatus = ex.getStatus();

        return new ResponseEntity<>(exceptionDto, httpStatus);
    }
}
