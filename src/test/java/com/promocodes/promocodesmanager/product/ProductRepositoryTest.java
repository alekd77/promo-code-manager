package com.promocodes.promocodesmanager.product;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
class ProductRepositoryTest {
    private final ProductRepository productRepository;

    @Autowired
    public ProductRepositoryTest(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @AfterEach
    void tearDown() {
        productRepository.deleteAll();
    }

    @Test
    public void shouldSaveProduct() {
        // Arrange
        Product product = new Product(
                null,
                "Apple",
                "Fruit",
                1.12,
                "USD"
        );

        // Act
        Product savedProduct = productRepository.save(product);

        // Assert
        assertThat(savedProduct.getProductId())
                .isNotNull();
        assertThat(savedProduct)
                .usingRecursiveComparison()
                .ignoringFields("productId")
                .isEqualTo(product);
    }

    @Test
    void shouldFindAllProducts() {
        // Arrange
        Product product1 = new Product(
                null,
                "Apple",
                "Fruit",
                1.12,
                "USD");
        Product product2 = new Product(
                null,
                "Banana",
                "Fruit",
                0.75,
                "USD");
        productRepository.save(product1);
        productRepository.save(product2);

        // Act
        List<Product> products = productRepository.findAll();

        // Assert
        assertThat(products)
                .hasSize(2)
                .contains(product1, product2);
    }

    @Test
    void shouldFindProductById() {
        // Arrange
        Product product = new Product(
                null,
                "Apple",
                "Fruit",
                1.12,
                "USD");
        Product savedProduct = productRepository.save(product);

        // Act
        Optional<Product> productFoundById = productRepository
                .findById(savedProduct.getProductId());

        // Assert
        assertThat(productFoundById).isPresent();
        assertThat(productFoundById.get())
                .usingRecursiveComparison()
                .ignoringFields("productId")
                .isEqualTo(savedProduct);
    }

    @Test
    void shouldCheckExistenceById() {
        // Arrange
        Product product = new Product(
                null,
                "Apple",
                "Fruit",
                1.12,
                "USD");
        Product savedProduct = productRepository.save(product);

        // Act
        boolean exists = productRepository
                .existsById(savedProduct.getProductId());

        // Assert
        assertThat(exists).isTrue();
    }

    @Test
    void shouldUpdateProduct() {
        // Arrange
        Product product = new Product(
                null,
                "Apple",
                "Fruit",
                1.12,
                "USD");
        Product savedProduct = productRepository.save(product);

        // Act
        savedProduct.setName("Banana");
        Product updatedProduct = productRepository.save(savedProduct);

        // Assert
        assertThat(updatedProduct.getName())
                .isEqualTo("Banana");
    }

    @Test
    void shouldCheckExistenceByName() {
        // Arrange
        Product product = new Product(
                null,
                "Apple",
                "Fruit",
                1.12,
                "USD");
        Product savedProduct = productRepository.save(product);

        // Act
        boolean exists = productRepository
                .existsByName("Apple");

        // Assert
        assertThat(exists).isTrue();
    }

    @Test
    void shouldFindProductByName() {
        // Arrange
        Product product = new Product(
                null,
                "Apple",
                "Fruit",
                1.12,
                "USD"
        );
        productRepository.save(product);
        String existingProductName = "Apple";

        // Act
        Optional<Product> productFoundByName = productRepository
                .findByName(existingProductName);

        // Assert
        assertThat(productFoundByName).isPresent();
        assertThat(productFoundByName.get())
                .usingRecursiveComparison()
                .ignoringFields("productId")
                .isEqualTo(product);
    }

    @Test
    void shouldReturnEmptyOptionalWhenProductNotFoundByName() {
        // Arrange
        Product product = new Product(
                null,
                "Apple",
                "Fruit",
                1.12,
                "USD"
        );
        productRepository.save(product);
        String nonExistingProductName = "NonExistingProduct";

        // Act
        Optional<Product> productFoundByName = productRepository
                .findByName(nonExistingProductName);

        // Assert
        assertThat(productFoundByName).isNotPresent();
    }
}