package com.promocodes.promocodesmanager.promocode;

import java.time.LocalDate;

public class FixedAmountPromoCodeDto {
    private String text;
    private LocalDate expirationDate;
    private Integer usagesAllowed;
    private Double discountAmount;
    private String discountCurrency;

    public FixedAmountPromoCodeDto() {
    }

    public FixedAmountPromoCodeDto(String text,
                                   LocalDate expirationDate,
                                   Integer usagesAllowed,
                                   Double discountAmount,
                                   String discountCurrency) {
        this.text = text;
        this.expirationDate = expirationDate;
        this.usagesAllowed = usagesAllowed;
        this.discountAmount = discountAmount;
        this.discountCurrency = discountCurrency;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public LocalDate getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(LocalDate expirationDate) {
        this.expirationDate = expirationDate;
    }

    public Integer getUsagesAllowed() {
        return usagesAllowed;
    }

    public void setUsagesAllowed(Integer usagesAllowed) {
        this.usagesAllowed = usagesAllowed;
    }

    public Double getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(Double discountAmount) {
        this.discountAmount = discountAmount;
    }

    public String getDiscountCurrency() {
        return discountCurrency;
    }

    public void setDiscountCurrency(String discountCurrency) {
        this.discountCurrency = discountCurrency;
    }
}
