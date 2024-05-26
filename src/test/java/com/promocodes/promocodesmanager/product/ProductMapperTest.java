package com.promocodes.promocodesmanager.product;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class ProductMapperTest {
    private ProductMapper productMapper;

    @BeforeEach
    void setUp() {
        productMapper = new ProductMapper();
    }

    @Test
    public void shouldReturnProductDto() {
        Product product = new Product(
                123L,
                "Apple",
                "Fruit",
                1.12,
                "USD"
        );

        ProductDto productDto = productMapper.toProductDto(product);

        assertThat(productDto.getId())
                .isEqualTo(product.getProductId());
        assertThat(productDto.getName())
                .isEqualTo(product.getName());
        assertThat(productDto.getDescription())
                .isEqualTo(product.getDescription());
        assertThat(productDto.getPrice())
                .isEqualTo(product.getPrice());
        assertThat(productDto.getCurrency())
                .isEqualTo(product.getCurrency());

    }

    @Test
    public void shouldReturnEmptyProductDtoIfProductIsNullable() {
        Product product = null;

        ProductDto productDto = productMapper.toProductDto(product);

        assertThat(productDto.getId())
                .isEqualTo(-1L);
        assertThat(productDto.getName())
                .isEqualTo("");
        assertThat(productDto.getDescription())
                .isEqualTo("");
        assertThat(productDto.getPrice())
                .isEqualTo(0.0);
        assertThat(productDto.getCurrency())
                .isEqualTo("");
    }

    @Test
    public void shouldReturnEmptyProductDtoIfProductFieldsAreNullable() {
        Product product = new Product(
                null,
                null,
                null,
                null,
                null
        );

        ProductDto productDto = productMapper.toProductDto(product);

        assertThat(productDto.getId())
                .isEqualTo(-1L);
        assertThat(productDto.getName())
                .isEqualTo("");
        assertThat(productDto.getDescription())
                .isEqualTo("");
        assertThat(productDto.getPrice())
                .isEqualTo(0.0);
        assertThat(productDto.getCurrency())
                .isEqualTo("");
    }
}