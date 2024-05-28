package com.promocodes.promocodesmanager.purchase;

import com.promocodes.promocodesmanager.product.Product;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "purchases")
public class Purchase {
    @Id
    @SequenceGenerator(
            name = "purchases_sequence",
            sequenceName = "purchases_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "purchases_sequence"
    )
    @Column(name="purchase_id")
    private Long purchaseId;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    private LocalDate purchaseDate;

    private Double regularPrice;

    private Double discountAmount;

    private String currency;

    public Purchase() {
    }

    public Purchase(Long purchaseId,
                    Product product,
                    LocalDate purchaseDate,
                    Double regularPrice,
                    Double discountAmount,
                    String currency) {
        this.purchaseId = purchaseId;
        this.product = product;
        this.purchaseDate = purchaseDate;
        this.regularPrice = regularPrice;
        this.discountAmount = discountAmount;
        this.currency = currency;
    }

    public Long getPurchaseId() {
        return purchaseId;
    }

    public void setPurchaseId(Long purchaseId) {
        this.purchaseId = purchaseId;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public LocalDate getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(LocalDate purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public Double getRegularPrice() {
        return regularPrice;
    }

    public void setRegularPrice(Double regularPrice) {
        this.regularPrice = regularPrice;
    }

    public Double getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(Double discountAmount) {
        this.discountAmount = discountAmount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Purchase purchase = (Purchase) o;
        return Objects.equals(purchaseId, purchase.purchaseId)
                && Objects.equals(product, purchase.product)
                && Objects.equals(purchaseDate, purchase.purchaseDate)
                && Objects.equals(regularPrice, purchase.regularPrice)
                && Objects.equals(discountAmount, purchase.discountAmount)
                && Objects.equals(currency, purchase.currency);
    }

    @Override
    public int hashCode() {
        return Objects.hash(purchaseId,
                product,
                purchaseDate,
                regularPrice,
                discountAmount,
                currency
        );
    }

    @Override
    public String toString() {
        return "Purchase{" +
                "purchaseId=" + purchaseId +
                ", product=" + product +
                ", purchaseDate=" + purchaseDate +
                ", regularPrice=" + regularPrice +
                ", discountAmount=" + discountAmount +
                ", currency='" + currency + '\'' +
                '}';
    }
}
