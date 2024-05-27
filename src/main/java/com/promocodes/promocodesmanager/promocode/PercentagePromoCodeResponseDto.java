package com.promocodes.promocodesmanager.promocode;

import java.time.LocalDate;

public class PercentagePromoCodeResponseDto extends PromoCodeResponseDto {
    private Integer discountPercentage;

    public PercentagePromoCodeResponseDto() {
        super();
    }

    public PercentagePromoCodeResponseDto(Long promoCodeId,
                                          String text,
                                          LocalDate expirationDate,
                                          Integer usagesTotal,
                                          Integer usagesLeft,
                                          PromoCodeType type,
                                          Integer discountPercentage) {
        super(promoCodeId, text, expirationDate, usagesTotal, usagesLeft, type);
        this.discountPercentage = discountPercentage;
    }

    public Integer getDiscountPercentage() {
        return discountPercentage;
    }

    public void setDiscountPercentage(Integer discountPercentage) {
        this.discountPercentage = discountPercentage;
    }
}
