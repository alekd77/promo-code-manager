package com.promocodes.promocodesmanager.report.sales;

public class SalesReportByCurrencyEntryDto {
    private String currency;
    private Integer numberOfPurchases;
    private Double totalRevenue;
    private Double totalDiscount;
    private Double totalNetRevenue;

    public SalesReportByCurrencyEntryDto() {
    }

    public SalesReportByCurrencyEntryDto(String currency,
                                         Integer numberOfPurchases,
                                         Double totalRevenue,
                                         Double totalDiscount,
                                         Double totalNetRevenue) {
        this.currency = currency;
        this.numberOfPurchases = numberOfPurchases;
        this.totalRevenue = totalRevenue;
        this.totalDiscount = totalDiscount;
        this.totalNetRevenue = totalNetRevenue;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Integer getNumberOfPurchases() {
        return numberOfPurchases;
    }

    public void setNumberOfPurchases(Integer numberOfPurchases) {
        this.numberOfPurchases = numberOfPurchases;
    }

    public Double getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(Double totalRevenue) {
        this.totalRevenue = totalRevenue;
    }

    public Double getTotalDiscount() {
        return totalDiscount;
    }

    public void setTotalDiscount(Double totalDiscount) {
        this.totalDiscount = totalDiscount;
    }

    public Double getTotalNetRevenue() {
        return totalNetRevenue;
    }

    public void setTotalNetRevenue(Double totalNetRevenue) {
        this.totalNetRevenue = totalNetRevenue;
    }
}
