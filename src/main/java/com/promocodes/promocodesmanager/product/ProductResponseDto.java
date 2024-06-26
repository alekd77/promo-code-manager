package com.promocodes.promocodesmanager.product;

public class ProductResponseDto {
    private Long id;
    private String name;
    private String description;
    private Double price;
    private String currency;

    public ProductResponseDto() {
    }

    public ProductResponseDto(Long id,
                              String name,
                              String description,
                              Double price,
                              String currency) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.currency = currency;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
