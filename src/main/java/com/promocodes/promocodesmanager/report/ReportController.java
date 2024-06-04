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

    @GetMapping(path = "/sales/by-currency", params = {"startDate", "endDate"})
    public ResponseEntity<SalesReportByCurrencyResponseDto> generateSalesByCurrencyReport(
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate startDate,
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate endDate) {
        SalesReportByCurrencyResponseDto responseDto =
                salesReportService.generateSalesByCurrencyReport(
                        startDate,
                        endDate
                );

        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }


}
