package com.promocodes.promocodesmanager.product;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Service
public class ProductMapper {
    ProductDto toProductDto(Product product) {
        ProductDto productDto = new ProductDto();

        productDto.setId(
                product != null && product.getProductId() != null
                        ? product.getProductId()
                        : -1L
        );
        productDto.setName(
                product != null && product.getName() != null
                        ? product.getName()
                        : ""
        );
        productDto.setDescription(
                product != null && product.getDescription() != null
                        ? product.getDescription()
                        : ""
        );
        productDto.setPrice(
                product != null && product.getPrice() != null
                        ? product.getPrice()
                        : 0
        );
        productDto.setCurrency(
                product != null && product.getCurrency() != null
                        ? product.getCurrency()
                        : ""
        );

        return productDto;
    }

    public List<ProductDto> toProductsDtoList(List<Product> productsList) {
        return productsList == null || productsList.isEmpty()
                ? Collections.emptyList()
                : productsList
                .stream()
                .map(this::toProductDto)
                .collect(Collectors.toList());
    }
}
