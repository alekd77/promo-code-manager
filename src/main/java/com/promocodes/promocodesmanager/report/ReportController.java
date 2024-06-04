package com.promocodes.promocodesmanager.report;

import com.promocodes.promocodesmanager.report.sales.SalesReportByCurrencyResponseDto;
import com.promocodes.promocodesmanager.report.sales.SalesReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping(path = "/reports")
public class ReportController {
    private final SalesReportService salesReportService;

    @Autowired
    public ReportController(SalesReportService salesReportService) {
        this.salesReportService = salesReportService;
    }

    @GetMapping(path = "/sales/by-currency")
    public ResponseEntity<SalesReportByCurrencyResponseDto> generateSalesByCurrencyReport(
            @RequestParam(name = "startDate", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate startDate,
            @RequestParam(name = "endDate", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate endDate) {

        if (startDate == null && endDate == null) {
            startDate = LocalDate.ofEpochDay(0);
            endDate = LocalDate.now();
        }

        SalesReportByCurrencyResponseDto responseDto =
                salesReportService.generateSalesByCurrencyReport(
                        startDate,
                        endDate
                );

        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

}
