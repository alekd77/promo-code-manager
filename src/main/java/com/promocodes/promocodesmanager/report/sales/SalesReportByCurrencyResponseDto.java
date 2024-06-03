package com.promocodes.promocodesmanager.report.sales;

import com.promocodes.promocodesmanager.report.ReportResponseDto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class SalesReportByCurrencyResponseDto extends ReportResponseDto {
    private List<SalesReportByCurrencyEntryDto> entries;

    public SalesReportByCurrencyResponseDto() {
        super();
        this.entries = new ArrayList<>();
    }

    public SalesReportByCurrencyResponseDto(LocalDate startDate,
                                            LocalDate endDate) {
        super("SALES", "BY_CURRENCY", startDate, endDate);
        this.entries = new ArrayList<>();
    }

    public SalesReportByCurrencyResponseDto(LocalDate startDate,
                                            LocalDate endDate,
                                            List<SalesReportByCurrencyEntryDto> entries) {
        super("SALES", "BY_CURRENCY", startDate, endDate);
        this.entries = entries;
    }

    public List<SalesReportByCurrencyEntryDto> getEntries() {
        return entries;
    }

    public void setEntries(List<SalesReportByCurrencyEntryDto> entries) {
        this.entries = entries;
    }
}
