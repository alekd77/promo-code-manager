package com.promocodes.promocodesmanager.report;

import com.promocodes.promocodesmanager.exception.ApiExceptionDto;
import com.promocodes.promocodesmanager.exception.ExceptionMapper;
import com.promocodes.promocodesmanager.exception.FailedToGenerateReportException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;

@RestControllerAdvice
public class ReportExceptionHandler {
    private final ExceptionMapper exceptionMapper;

    @Autowired
    public ReportExceptionHandler(ExceptionMapper exceptionMapper) {
        this.exceptionMapper = exceptionMapper;
    }

    @ExceptionHandler({FailedToGenerateReportException.class})
    public ResponseEntity<ApiExceptionDto> handleFailedToGenerateReportException(
            FailedToGenerateReportException ex) {
        ApiExceptionDto exceptionDto = exceptionMapper.toApiExceptionDto(ex);
        HttpStatus httpStatus = ex.getStatus();

        return new ResponseEntity<>(exceptionDto, httpStatus);
    }

    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    public ResponseEntity<ApiExceptionDto> handleMethodArgumentTypeMismatchException(
            MethodArgumentTypeMismatchException ex) {
        String invalidValue = ex.getValue() != null
                ? ex.getValue().toString()
                : "null";
        String fieldName = ex.getParameter()
                .getParameter()
                .getName();

        ApiExceptionDto exceptionDto = new ApiExceptionDto();
        exceptionDto.setMessage("Invalid request parameter format.");
        exceptionDto.setTimestamp(LocalDateTime.now());
        exceptionDto.setHttpStatus(HttpStatus.BAD_REQUEST);
        exceptionDto.setDebugMessage(String.format(
                "Invalid date format '%s' for '%s'. " +
                        "Expected format is 'YYYY-MM-DD'.",
                invalidValue,
                fieldName
        ));

        return new ResponseEntity<>(exceptionDto, HttpStatus.BAD_REQUEST);
    }

}
