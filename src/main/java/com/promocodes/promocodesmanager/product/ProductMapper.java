package com.promocodes.promocodesmanager.product;

import org.springframework.stereotype.Service;

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
}
