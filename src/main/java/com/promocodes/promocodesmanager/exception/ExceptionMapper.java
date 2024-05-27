package com.promocodes.promocodesmanager.exception;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ExceptionMapper {
    public ApiExceptionDto toApiExceptionDto(ApiException ex) {
        ApiExceptionDto exceptionDto = new ApiExceptionDto();

        exceptionDto.setMessage(
                ex != null
                        && ex.getMessage() != null
                        && !ex.getMessage().isEmpty()
                        ? ex.getMessage()
                        : "Unknown error occurred"
        );

        exceptionDto.setTimestamp(
                ex != null && ex.getTimestamp() != null
                        ? ex.getTimestamp()
                        : LocalDateTime.now()
        );

        exceptionDto.setHttpStatus(
                ex != null && ex.getStatus() != null
                        ? ex.getStatus()
                        : HttpStatus.INTERNAL_SERVER_ERROR
        );

        exceptionDto.setDebugMessage(
                ex != null && ex.getDebugMessage() != null
                        ? ex.getDebugMessage()
                        : ""
        );

        return exceptionDto;
    }
}
