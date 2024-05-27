package com.promocodes.promocodesmanager.product;

import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductMapper {
    ProductResponseDto toProductDto(Product product) {
        ProductResponseDto productResponseDto = new ProductResponseDto();

        productResponseDto.setId(
                product != null && product.getProductId() != null
                        ? product.getProductId()
                        : -1L
        );
        productResponseDto.setName(
                product != null && product.getName() != null
                        ? product.getName()
                        : ""
        );
        productResponseDto.setDescription(
                product != null && product.getDescription() != null
                        ? product.getDescription()
                        : ""
        );
        productResponseDto.setPrice(
                product != null && product.getPrice() != null
                        ? product.getPrice()
                        : 0
        );
        productResponseDto.setCurrency(
                product != null && product.getCurrency() != null
                        ? product.getCurrency()
                        : ""
        );

        return productResponseDto;
    }

    public List<ProductResponseDto> toProductsDtoList(List<Product> productsList) {
        return productsList == null || productsList.isEmpty()
                ? Collections.emptyList()
                : productsList
                .stream()
                .map(this::toProductDto)
                .collect(Collectors.toList());
    }
}
