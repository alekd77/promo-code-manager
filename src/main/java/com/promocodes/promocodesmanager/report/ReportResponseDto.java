package com.promocodes.promocodesmanager.report;

import java.time.LocalDate;

public abstract class ReportResponseDto {
    private String category;
    private String name;
    private LocalDate startDate;
    private LocalDate endDate;

    public ReportResponseDto() {
    }

    public ReportResponseDto(String category,
                             String name,
                             LocalDate startDate,
                             LocalDate endDate) {
        this.category = category;
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
}
