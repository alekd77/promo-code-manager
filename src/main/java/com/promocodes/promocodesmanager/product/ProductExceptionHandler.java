package com.promocodes.promocodesmanager.product;

import com.promocodes.promocodesmanager.exception.*;
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

    @ExceptionHandler({ProductDiscountCalculationException.class})
    public ResponseEntity<DiscountProductPriceResponseDto> handleProductDiscountCalculationException(
            ProductDiscountCalculationException ex) {
        DiscountProductPriceResponseDto dto =
                new DiscountProductPriceResponseDto();
        dto.setDiscountedPrice(ex.getProduct().getPrice());
        dto.setWarningMessage(
                String.format(
                        "%s\n%s",
                        ex.getMessage(),
                        ex.getDebugMessage()
                )
        );

        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @ExceptionHandler({FailedToAddNewProductException.class})
    public ResponseEntity<ApiExceptionDto> handleFailedToAddNewProductException(
            FailedToAddNewProductException ex) {
        ApiExceptionDto exceptionDto = exceptionMapper.toApiExceptionDto(ex);
        HttpStatus httpStatus = ex.getStatus();

        return new ResponseEntity<>(exceptionDto, httpStatus);
    }

    @ExceptionHandler({FailedToUpdateProductException.class})
    public ResponseEntity<ApiExceptionDto> handleFailedToUpdateProductException(
            FailedToUpdateProductException ex) {
        ApiExceptionDto exceptionDto = exceptionMapper.toApiExceptionDto(ex);
        HttpStatus httpStatus = ex.getStatus();

        return new ResponseEntity<>(exceptionDto, httpStatus);
    }
}
