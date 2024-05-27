package com.promocodes.promocodesmanager.product;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

        ProductResponseDto productResponseDto = productMapper.toProductDto(product);

        assertThat(productResponseDto.getId())
                .isEqualTo(product.getProductId());
        assertThat(productResponseDto.getName())
                .isEqualTo(product.getName());
        assertThat(productResponseDto.getDescription())
                .isEqualTo(product.getDescription());
        assertThat(productResponseDto.getPrice())
                .isEqualTo(product.getPrice());
        assertThat(productResponseDto.getCurrency())
                .isEqualTo(product.getCurrency());

    }

    @Test
    public void shouldReturnEmptyProductDtoIfProductIsNullable() {
        Product product = null;

        ProductResponseDto productResponseDto = productMapper.toProductDto(product);

        assertThat(productResponseDto.getId())
                .isEqualTo(-1L);
        assertThat(productResponseDto.getName())
                .isEqualTo("");
        assertThat(productResponseDto.getDescription())
                .isEqualTo("");
        assertThat(productResponseDto.getPrice())
                .isEqualTo(0.0);
        assertThat(productResponseDto.getCurrency())
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

        ProductResponseDto productResponseDto = productMapper.toProductDto(product);

        assertThat(productResponseDto.getId())
                .isEqualTo(-1L);
        assertThat(productResponseDto.getName())
                .isEqualTo("");
        assertThat(productResponseDto.getDescription())
                .isEqualTo("");
        assertThat(productResponseDto.getPrice())
                .isEqualTo(0.0);
        assertThat(productResponseDto.getCurrency())
                .isEqualTo("");
    }

    @Test
    public void shouldReturnProductsDtoList() {
        List<Product> productsList = Arrays.asList(
                new Product(
                        123L,
                        "Apple",
                        "Fruit",
                        1.12,
                        "USD"
                ),
                new Product(
                        233L,
                        "Banana",
                        "Fruit",
                        0.75,
                        "USD"
                )
        );

        List<ProductResponseDto> productResponseDtoList = productMapper.toProductsDtoList(productsList);

        assertThat(productResponseDtoList).isNotNull();
        assertThat(productResponseDtoList.size()).isEqualTo(2);
        assertThat(productResponseDtoList.get(0).getId()).isEqualTo(123L);
        assertThat(productResponseDtoList.get(0).getName()).isEqualTo("Apple");
        assertThat(productResponseDtoList.get(0).getDescription()).isEqualTo("Fruit");
        assertThat(productResponseDtoList.get(0).getPrice()).isEqualTo(1.12);
        assertThat(productResponseDtoList.get(0).getCurrency()).isEqualTo("USD");
        assertThat(productResponseDtoList.get(1).getId()).isEqualTo(233L);
        assertThat(productResponseDtoList.get(1).getName()).isEqualTo("Banana");
        assertThat(productResponseDtoList.get(1).getDescription()).isEqualTo("Fruit");
        assertThat(productResponseDtoList.get(1).getPrice()).isEqualTo(0.75);
        assertThat(productResponseDtoList.get(1).getCurrency()).isEqualTo("USD");
    }

    @Test
    public void shouldReturnEmptyProductsDtoListIfProductListIsNullable() {
        List<Product> productsList = null;

        List<ProductResponseDto> productResponseDtoList = productMapper.toProductsDtoList(productsList);

        assertThat(productResponseDtoList).isNotNull();
        assertThat(productResponseDtoList.size()).isEqualTo(0);
    }

    @Test
    public void shouldReturnEmptyProductsDtoListIfProductsListIsEmpty() {
        List<Product> productsList = new ArrayList<>();

        List<ProductResponseDto> productResponseDtoList = productMapper.toProductsDtoList(productsList);

        assertThat(productResponseDtoList).isNotNull();
        assertThat(productResponseDtoList.size()).isEqualTo(0);
    }
}