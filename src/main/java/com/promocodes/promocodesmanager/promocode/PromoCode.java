package com.promocodes.promocodesmanager.promocode;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "promo_codes")
public class PromoCode {
    @Id
    @SequenceGenerator(
            name = "promo_codes_sequence",
            sequenceName = "promo_codes_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "promo_codes_sequence"
    )
    @Column(name="promo_code_id")
    private Long promoCodeId;

    private String text;

    @Column(name="expiration_date")
    private LocalDate expirationDate;

    @Column(name="usages_total")
    private Integer usagesTotal;

    @Column(name="usages_left")
    private Integer usagesLeft;

    @Enumerated(EnumType.STRING)
    private PromoCodeType type;

    @Column(name = "discount_amount")
    private Double discountAmount;

    @Column(name = "discount_currency")
    private String discountCurrency;

    @Column(name = "discount_percentage")
    private Double discountPercentage;

    public PromoCode() {
    }

    public PromoCode(Long promoCodeId,
                     String text,
                     LocalDate expirationDate,
                     Integer usagesTotal,
                     Integer usagesLeft,
                     PromoCodeType type,
                     Double discountAmount,
                     String discountCurrency,
                     Double discountPercentage) {
        this.promoCodeId = promoCodeId;
        this.text = text;
        this.expirationDate = expirationDate;
        this.usagesTotal = usagesTotal;
        this.usagesLeft = usagesLeft;
        this.type = type;
        this.discountAmount = discountAmount;
        this.discountCurrency = discountCurrency;
        this.discountPercentage = discountPercentage;
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

    public Double getDiscountPercentage() {
        return discountPercentage;
    }

    public void setDiscountPercentage(Double discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PromoCode promoCode = (PromoCode) o;
        return Objects.equals(promoCodeId, promoCode.promoCodeId) && Objects.equals(text, promoCode.text) && Objects.equals(expirationDate, promoCode.expirationDate) && Objects.equals(usagesTotal, promoCode.usagesTotal) && Objects.equals(usagesLeft, promoCode.usagesLeft) && type == promoCode.type && Objects.equals(discountAmount, promoCode.discountAmount) && Objects.equals(discountCurrency, promoCode.discountCurrency) && Objects.equals(discountPercentage, promoCode.discountPercentage);
    }

    @Override
    public int hashCode() {
        return Objects.hash(promoCodeId, text, expirationDate, usagesTotal, usagesLeft, type, discountAmount, discountCurrency, discountPercentage);
    }

    @Override
    public String toString() {
        return "PromoCode{" +
                "promoCodeId=" + promoCodeId +
                ", text='" + text + '\'' +
                ", expirationDate=" + expirationDate +
                ", usagesTotal=" + usagesTotal +
                ", usagesLeft=" + usagesLeft +
                ", type=" + type +
                ", discountAmount=" + discountAmount +
                ", discountCurrency='" + discountCurrency + '\'' +
                ", discountPercentage=" + discountPercentage +
                '}';
    }
}
