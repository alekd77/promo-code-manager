package com.promocodes.promocodesmanager.exception;

import org.springframework.http.HttpStatus;

public class FailedToGenerateReportException extends ApiException {
    public FailedToGenerateReportException(HttpStatus httpStatus,
                                           String debugMessage) {
        super("Failed to generate report.", httpStatus, debugMessage);
    }
}
