package com.promocodes.promocodesmanager.product;

import com.promocodes.promocodesmanager.exception.FailedToAddNewProductException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Currency;
import java.util.List;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Transactional
    public Product addNewProduct(String name,
                                 String description,
                                 Double price,
                                 String currency) {
        if (name == null || name.isEmpty()) {
            throw new FailedToAddNewProductException(
                    HttpStatus.BAD_REQUEST,
                    "Name of new product can not be null or empty"
            );
        }

        if (productRepository.existsByName(name)) {
            throw new FailedToAddNewProductException(
                    HttpStatus.BAD_REQUEST,
                    String.format("'%s' product name is taken", name)
            );
        }

        if (price == null) {
            throw new FailedToAddNewProductException(
                    HttpStatus.BAD_REQUEST,
                    "Price of new product can not be null or empty"
            );
        }

        if (price < 0) {
            throw new FailedToAddNewProductException(
                    HttpStatus.BAD_REQUEST,
                    "Price of new product can not be less than zero"
            );
        }

        if (currency == null || currency.isEmpty()) {
            throw new FailedToAddNewProductException(
                    HttpStatus.BAD_REQUEST,
                    "Currency of new product can not be null or empty"
            );
        }

        if (!isValidCurrency(currency)) {
            throw new FailedToAddNewProductException(
                    HttpStatus.BAD_REQUEST,
                    String.format("Currency '%s' is invalid", currency)
            );
        }

        try {
            String formatedName = StringUtils.capitalize(name);
            String formatedDescription = StringUtils.capitalize(description);
            Double formatedPrice = BigDecimal.valueOf(price)
                    .setScale(2, RoundingMode.DOWN)
                    .doubleValue();
            String formattedCurrency = currency.toUpperCase();

            Product product = new Product();
            product.setName(formatedName);
            product.setDescription(formatedDescription);
            product.setPrice(formatedPrice);
            product.setCurrency(formattedCurrency);

            return productRepository.save(product);
        } catch (Exception ex) {
            throw new FailedToAddNewProductException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Unknown error occurred"
            );
        }
    }

    private boolean isValidCurrency(String currency) {
        try {
            String formattedCurrency = currency.toUpperCase();

            return Currency.getAvailableCurrencies()
                    .contains(Currency.getInstance(formattedCurrency));
        } catch (Exception ex) {
            return false;
        }
    }
}
