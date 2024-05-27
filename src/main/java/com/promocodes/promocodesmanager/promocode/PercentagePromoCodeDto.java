package com.promocodes.promocodesmanager.promocode;

import java.time.LocalDate;

public class PercentagePromoCodeDto {
    private String text;
    private LocalDate expirationDate;
    private Integer usagesAllowed;
    private Integer discountPercentage;

    public PercentagePromoCodeDto() {
    }

    public PercentagePromoCodeDto(String text,
                                  LocalDate expirationDate,
                                  Integer usagesAllowed,
                                  Integer discountPercentage) {
        this.text = text;
        this.expirationDate = expirationDate;
        this.usagesAllowed = usagesAllowed;
        this.discountPercentage = discountPercentage;
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

    public Integer getDiscountPercentage() {
        return discountPercentage;
    }

    public void setDiscountPercentage(Integer discountPercentage) {
        this.discountPercentage = discountPercentage;
    }
}
