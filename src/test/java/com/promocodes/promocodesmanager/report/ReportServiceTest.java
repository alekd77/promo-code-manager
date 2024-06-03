package com.promocodes.promocodesmanager.report;

import com.promocodes.promocodesmanager.exception.FailedToAddNewProductException;
import com.promocodes.promocodesmanager.exception.FailedToGenerateReportException;
import org.assertj.core.api.NotThrownAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.*;

public class ReportServiceTest {
    private ReportService reportService;

    @BeforeEach
    void setUp() {
        reportService = new ReportService();
    }

    @Test
    public void shouldValidateReportDatesValidDataRange() {
        LocalDate startDate = LocalDate.now().minusDays(30);
        LocalDate endDate = LocalDate.now().minusDays(10);

        assertThatCode(() -> reportService.validateReportDates(startDate, endDate))
                .doesNotThrowAnyException();
    }

    @Test
    public void shouldValidateReportDatesWithStartDateSameAsEndDate() {
        LocalDate startDate = LocalDate.now().minusDays(10);
        LocalDate endDate = LocalDate.now().minusDays(10);

        assertThatCode(() -> reportService.validateReportDates(startDate, endDate))
                .doesNotThrowAnyException();
    }

    @Test
    public void shouldValidateReportDatesWithEndDateToday() {
        LocalDate startDate = LocalDate.now().minusDays(30);
        LocalDate endDate = LocalDate.now();

        assertThatCode(() -> reportService.validateReportDates(startDate, endDate))
                .doesNotThrowAnyException();

    }

    @Test
    public void shouldValidateReportDatesWithStartAndEndDateToday() {
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = LocalDate.now();

        assertThatCode(() -> reportService.validateReportDates(startDate, endDate))
               .doesNotThrowAnyException();
    }

    @Test
    public void shouldThrowFailedToGenerateReportExceptionWithStartDateNull() {
        LocalDate startDate = null;
        LocalDate endDate = LocalDate.now().minusDays(10);

        assertThatThrownBy(() -> reportService.validateReportDates(startDate, endDate))
                .isInstanceOf(FailedToGenerateReportException.class)
                .extracting(
                        "message",
                        "status",
                        "debugMessage"
                )
                .containsExactly(
                        "Failed to generate report.",
                        HttpStatus.BAD_REQUEST,
                        "Report dates can not be null."
                );
    }

    @Test
    public void shouldThrowFailedToGenerateReportExceptionWithEndDateNull() {
        LocalDate startDate = LocalDate.now().minusDays(10);
        LocalDate endDate = null;

        assertThatThrownBy(() -> reportService.validateReportDates(startDate, endDate))
               .isInstanceOf(FailedToGenerateReportException.class)
               .extracting(
                       "message",
                        "status",
                        "debugMessage"
                )
               .containsExactly(
                        "Failed to generate report.",
                        HttpStatus.BAD_REQUEST,
                        "Report dates can not be null."
                );
    }

    @Test
    public void shouldThrowFailedToGenerateReportExceptionWithStartDateAfterEndDate() {
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = LocalDate.now().minusDays(10);

        assertThatThrownBy(() -> reportService.validateReportDates(startDate, endDate))
               .isInstanceOf(FailedToGenerateReportException.class)
               .extracting(
                        "message",
                        "status",
                        "debugMessage"
                )
               .containsExactly(
                        "Failed to generate report.",
                        HttpStatus.BAD_REQUEST,
                        "Report start date must be " +
                                "before or equal end date " +
                                "(startDate <= endDate)."
                );
    }

    @Test
    public void shouldThrowFailedToGenerateReportExceptionWithDatesFromTheFuture() {
        LocalDate startDate = LocalDate.now().plusDays(10);
        LocalDate endDate = LocalDate.now().plusDays(30);

        assertThatThrownBy(() -> reportService.validateReportDates(startDate, endDate))
               .isInstanceOf(FailedToGenerateReportException.class)
               .extracting(
                        "message",
                        "status",
                        "debugMessage"
                )
               .containsExactly(
                        "Failed to generate report.",
                        HttpStatus.BAD_REQUEST,
                        "Report dates can not be from the future."
                );
    }
}
