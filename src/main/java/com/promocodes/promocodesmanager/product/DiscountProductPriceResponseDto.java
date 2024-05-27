package com.promocodes.promocodesmanager.product;

public class DiscountProductPriceResponseDto {
    private Double discountedPrice;
    private String warningMessage;

    public DiscountProductPriceResponseDto() {
    }

    public DiscountProductPriceResponseDto(Double discountedPrice,
                                           String warningMessage) {
        this.discountedPrice = discountedPrice;
        this.warningMessage = warningMessage;
    }

    public Double getDiscountedPrice() {
        return discountedPrice;
    }

    public void setDiscountedPrice(Double discountedPrice) {
        this.discountedPrice = discountedPrice;
    }

    public String getWarningMessage() {
        return warningMessage;
    }

    public void setWarningMessage(String warningMessage) {
        this.warningMessage = warningMessage;
    }
}
