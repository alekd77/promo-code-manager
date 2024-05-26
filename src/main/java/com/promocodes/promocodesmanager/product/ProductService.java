package com.promocodes.promocodesmanager.product;

import com.promocodes.promocodesmanager.exception.FailedToAddNewProductException;
import com.promocodes.promocodesmanager.exception.FailedToUpdateProductException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Currency;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

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

    @Transactional
    public Product updateProduct(String name,
                                 String newName,
                                 String newDescription,
                                 Double newPrice,
                                 String newCurrency) {

        Product product = findProductByName(name);

        if (product == null) {
            throw new FailedToUpdateProductException(
                    HttpStatus.BAD_REQUEST,
                    String.format("Product '%s' does not exist.", name)
            );
        }

        if (newName != null && !newName.isEmpty()) {
            String formattedNewName = StringUtils.capitalize(newName);

            if (!Objects.equals(product.getName(), formattedNewName)
                    && productRepository.existsByName(formattedNewName)) {
                throw new FailedToUpdateProductException(
                        HttpStatus.BAD_REQUEST,
                        String.format("Product '%s' already exists.",
                                formattedNewName
                        )
                );
            }

            product.setName(formattedNewName);
        }

        if (newDescription != null) {
            String formattedNewDescription =
                    StringUtils.capitalize(newDescription);
            product.setDescription(formattedNewDescription);
        }

        if (newPrice != null) {
            if (newPrice < 0) {
                throw new FailedToUpdateProductException(
                        HttpStatus.BAD_REQUEST,
                        "Price of the product can not be less than zero."
                );
            }

            Double formatedNewPrice = BigDecimal.valueOf(newPrice)
                    .setScale(2, RoundingMode.DOWN)
                    .doubleValue();
            product.setPrice(formatedNewPrice);
        }

        if (newCurrency != null) {
            if (!isValidCurrency(newCurrency)) {
                throw new FailedToUpdateProductException(
                        HttpStatus.BAD_REQUEST,
                        String.format("Currency '%s' is not valid.", newCurrency)
                );
            }

            String formattedNewCurrency = newCurrency.toUpperCase();
            product.setCurrency(formattedNewCurrency);
        }

        try {
            return productRepository.save(product);
        } catch (Exception ex) {
            throw new FailedToUpdateProductException(
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

    private Product findProductByName(String name) {
        if (name == null || name.isEmpty()) {
            return null;
        }

        String formattedName = StringUtils.capitalize(name);
        Optional<Product> optionalProduct =
                productRepository.findByName(formattedName);

        return optionalProduct.orElse(null);
    }
}
