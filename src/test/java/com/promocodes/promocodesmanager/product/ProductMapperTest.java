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

        List<ProductDto> productDtoList = productMapper.toProductsDtoList(productsList);

        assertThat(productDtoList).isNotNull();
        assertThat(productDtoList.size()).isEqualTo(2);
        assertThat(productDtoList.get(0).getId()).isEqualTo(123L);
        assertThat(productDtoList.get(0).getName()).isEqualTo("Apple");
        assertThat(productDtoList.get(0).getDescription()).isEqualTo("Fruit");
        assertThat(productDtoList.get(0).getPrice()).isEqualTo(1.12);
        assertThat(productDtoList.get(0).getCurrency()).isEqualTo("USD");
        assertThat(productDtoList.get(1).getId()).isEqualTo(233L);
        assertThat(productDtoList.get(1).getName()).isEqualTo("Banana");
        assertThat(productDtoList.get(1).getDescription()).isEqualTo("Fruit");
        assertThat(productDtoList.get(1).getPrice()).isEqualTo(0.75);
        assertThat(productDtoList.get(1).getCurrency()).isEqualTo("USD");
    }

    @Test
    public void shouldReturnEmptyProductsDtoListIfProductListIsNullable() {
        List<Product> productsList = null;

        List<ProductDto> productDtoList = productMapper.toProductsDtoList(productsList);

        assertThat(productDtoList).isNotNull();
        assertThat(productDtoList.size()).isEqualTo(0);
    }

    @Test
    public void shouldReturnEmptyProductsDtoListIfProductsListIsEmpty() {
        List<Product> productsList = new ArrayList<>();

        List<ProductDto> productDtoList = productMapper.toProductsDtoList(productsList);

        assertThat(productDtoList).isNotNull();
        assertThat(productDtoList.size()).isEqualTo(0);
    }
}