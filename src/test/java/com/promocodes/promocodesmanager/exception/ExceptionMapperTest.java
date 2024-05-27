package com.promocodes.promocodesmanager.exception;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ExceptionMapperTest {
    private ExceptionMapper exceptionMapper;

    @BeforeEach
    void setUp() {
        exceptionMapper = new ExceptionMapper();
    }

    @Test
    public void shouldReturnApiExceptionDto() {
        ApiException apiException = new ApiException(
                "Test message",
                HttpStatus.BAD_REQUEST,
                "Debug details"
        );

        ApiExceptionDto dto = exceptionMapper.toApiExceptionDto(apiException);

        assertEquals(dto.getMessage(), apiException.getMessage());
        assertEquals(dto.getTimestamp(), apiException.getTimestamp());
        assertEquals(dto.getHttpStatus(), apiException.getStatus());
        assertEquals(dto.getDebugMessage(), apiException.getDebugMessage());
    }

    @Test
    public void shouldReturnGenericApiExceptionMessageIfNotSpecified() {
        ApiException apiException = new ApiException(
                "",
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Debug details"
        );

        ApiExceptionDto dto = exceptionMapper.toApiExceptionDto(apiException);

        assertEquals(dto.getMessage(), "Unknown error occurred");
        assertEquals(dto.getTimestamp(), apiException.getTimestamp()); // Should be current timestamp
        assertEquals(dto.getHttpStatus(), apiException.getStatus());
        assertEquals(dto.getDebugMessage(), apiException.getDebugMessage());
    }

    @Test
    public void shouldReturnGenericApiExceptionDtoIfExceptionIsNullable() {
        ApiExceptionDto dto = exceptionMapper.toApiExceptionDto(null);

        assertEquals(dto.getMessage(), "Unknown error occurred");
        assertNotNull(dto.getTimestamp());
        assertEquals(dto.getHttpStatus(), HttpStatus.INTERNAL_SERVER_ERROR);
        assertEquals(dto.getDebugMessage(), "");
    }

    @Test
    public void shouldReturnValidApiExceptionDtoIfCustomExceptionThrown() {
        ApiException apiException = new FailedToAddNewProductException(
                HttpStatus.BAD_REQUEST,
                "Name of new product can not be null or empty"
        );

        ApiExceptionDto dto = exceptionMapper.toApiExceptionDto(apiException);

        assertEquals(dto.getMessage(), apiException.getMessage());
        assertEquals(dto.getTimestamp(), apiException.getTimestamp());
        assertEquals(dto.getHttpStatus(), apiException.getStatus());
        assertEquals(dto.getDebugMessage(), apiException.getDebugMessage());
    }
}