package com.promocodes.promocodesmanager.promocode;

import java.time.LocalDate;

public class FixedAmountPromoCodeResponseDto extends PromoCodeResponseDto {
    private Double discountAmount;
    private String discountCurrency;

    public FixedAmountPromoCodeResponseDto() {
        super();
    }

    public FixedAmountPromoCodeResponseDto(Long promoCodeId,
                                           String text,
                                           LocalDate expirationDate,
                                           Integer usagesTotal,
                                           Integer usagesLeft,
                                           PromoCodeType type,
                                           Double discountAmount,
                                           String discountCurrency) {
        super(promoCodeId, text, expirationDate, usagesTotal, usagesLeft, type);
        this.discountAmount = discountAmount;
        this.discountCurrency = discountCurrency;
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
