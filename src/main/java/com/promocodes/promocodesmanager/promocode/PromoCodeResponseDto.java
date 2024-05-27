package com.promocodes.promocodesmanager.promocode;

import java.time.LocalDate;

public abstract class PromoCodeResponseDto {
    private Long promoCodeId;
    private String text;
    private LocalDate expirationDate;
    private Integer usagesTotal;
    private Integer usagesLeft;
    private PromoCodeType type;

    public PromoCodeResponseDto() {
    }

    public PromoCodeResponseDto(Long promoCodeId,
                                String text,
                                LocalDate expirationDate,
                                Integer usagesTotal,
                                Integer usagesLeft,
                                PromoCodeType type) {
        this.promoCodeId = promoCodeId;
        this.text = text;
        this.expirationDate = expirationDate;
        this.usagesTotal = usagesTotal;
        this.usagesLeft = usagesLeft;
        this.type = type;
    }

    public Long getPromoCodeId() {
        return promoCodeId;
    }

    public void setPromoCodeId(Long promoCodeId) {
        this.promoCodeId = promoCodeId;
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

    public Integer getUsagesTotal() {
        return usagesTotal;
    }

    public void setUsagesTotal(Integer usagesTotal) {
        this.usagesTotal = usagesTotal;
    }

    public Integer getUsagesLeft() {
        return usagesLeft;
    }

    public void setUsagesLeft(Integer usagesLeft) {
        this.usagesLeft = usagesLeft;
    }

    public PromoCodeType getType() {
        return type;
    }

    public void setType(PromoCodeType type) {
        this.type = type;
    }
}
