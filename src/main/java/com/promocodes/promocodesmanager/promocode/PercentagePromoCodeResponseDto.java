package com.promocodes.promocodesmanager.promocode;

import java.time.LocalDate;

public class PercentagePromoCodeResponseDto extends PromoCodeResponseDto {
    private Double discountPercentage;

    public PercentagePromoCodeResponseDto() {
        super();
    }

    public PercentagePromoCodeResponseDto(Long promoCodeId,
                                          String text,
                                          LocalDate expirationDate,
                                          Integer usagesTotal,
                                          Integer usagesLeft,
                                          PromoCodeType type,
                                          Double discountPercentage) {
        super(promoCodeId, text, expirationDate, usagesTotal, usagesLeft, type);
        this.discountPercentage = discountPercentage;
    }

    public Double getDiscountPercentage() {
        return discountPercentage;
    }

    public void setDiscountPercentage(Double discountPercentage) {
        this.discountPercentage = discountPercentage;
    }
}
