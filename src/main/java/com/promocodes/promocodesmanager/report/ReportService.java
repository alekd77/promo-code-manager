package com.promocodes.promocodesmanager.report;

import com.promocodes.promocodesmanager.exception.FailedToGenerateReportException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class ReportService {
    public void validateReportDates(LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null) {
            throw new FailedToGenerateReportException(
                    HttpStatus.BAD_REQUEST,
                    "Report dates can not be null."
            );
        }

        if (startDate.isAfter(endDate)) {
            throw new FailedToGenerateReportException(
                    HttpStatus.BAD_REQUEST,
                    "Report start date must be " +
                            "before or equal end date " +
                            "(startDate <= endDate)."
            );
        }

        if (endDate.isAfter(LocalDate.now())) {
            throw new FailedToGenerateReportException(
                    HttpStatus.BAD_REQUEST,
                    "Report dates can not be from the future."
            );
        }
    }
}
