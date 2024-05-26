package com.promocodes.promocodesmanager.product;

import com.promocodes.promocodesmanager.exception.FailedToAddNewProductException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
    @Mock
    private ProductRepository productRepository;
    private ProductService productService;

    @BeforeEach
    void setUp() {
        productService = new ProductService(productRepository);
    }

    @Test
    public void shouldReturnAllProducts() {
        productService.getAllProducts();

        verify(productRepository, times(1))
                .findAll();
    }

    @Test
    public void shouldAddNewProduct() {
        String name = "Apple";
        String description = "Fruit";
        Double price = 1.12;
        String currency = "USD";

        productService.addNewProduct(
                name,
                description,
                price,
                currency
        );

        ArgumentCaptor<Product> productArgumentCaptor =
                ArgumentCaptor.forClass(Product.class);

        verify(productRepository, times(1))
                .save(productArgumentCaptor.capture());

        assertThat(productArgumentCaptor.getValue().getName())
                .isEqualTo(name);
        assertThat(productArgumentCaptor.getValue().getDescription())
                .isEqualTo(description);
        assertThat(productArgumentCaptor.getValue().getPrice())
                .isEqualTo(price);
        assertThat(productArgumentCaptor.getValue().getCurrency())
                .isEqualTo(currency);
    }

    @Test
    public void shouldFormatAndAddNewProduct() {
        String name = "apple";
        String description = "sample fruit";
        Double price = 1.128390;
        String currency = "usd";

        productService.addNewProduct(
                name,
                description,
                price,
                currency
        );

        ArgumentCaptor<Product> productArgumentCaptor =
                ArgumentCaptor.forClass(Product.class);

        verify(productRepository, times(1))
                .save(productArgumentCaptor.capture());

        assertThat(productArgumentCaptor.getValue().getName())
                .isEqualTo("Apple");
        assertThat(productArgumentCaptor.getValue().getDescription())
                .isEqualTo("Sample fruit");
        assertThat(productArgumentCaptor.getValue().getPrice())
                .isEqualTo(1.12);
        assertThat(productArgumentCaptor.getValue().getCurrency())
                .isEqualTo("USD");
    }

    @Test
    public void shouldThrowFailedToAddNewProductExceptionIfNameIsNullable() {
        String name = null;
        String description = "Fruit";
        Double price = 1.12;
        String currency = "USD";

        assertThatThrownBy(() -> productService.addNewProduct(
                name,
                description,
                price,
                currency
        )).isInstanceOf(FailedToAddNewProductException.class)
                .extracting(
                        "message",
                        "status",
                        "debugMessage"
                )
                .containsExactly(
                        "Failed to add new product.",
                        HttpStatus.BAD_REQUEST,
                        "Name of new product can not be null or empty"
                );
    }

    @Test
    public void shouldThrowFailedToAddNewProductExceptionIfNameIsEmpty() {
        String name = "";
        String description = "Fruit";
        Double price = 1.12;
        String currency = "USD";

        assertThatThrownBy(() -> productService.addNewProduct(
                name,
                description,
                price,
                currency
        )).isInstanceOf(FailedToAddNewProductException.class)
                .extracting(
                        "message",
                        "status",
                        "debugMessage"
                )
                .containsExactly(
                        "Failed to add new product.",
                        HttpStatus.BAD_REQUEST,
                        "Name of new product can not be null or empty"
                );
    }

    @Test
    public void shouldThrowFailedToAddNewProductExceptionIfProductOfGivenNameAlreadyExists() {
        String name = "Apple";
        String description = "Fruit";
        Double price = 1.12;
        String currency = "USD";

        when(productRepository.existsByName(anyString()))
                .thenReturn(true);

        assertThatThrownBy(() -> productService.addNewProduct(
                name,
                description,
                price,
                currency
        )).isInstanceOf(FailedToAddNewProductException.class)
                .extracting(
                        "message",
                        "status",
                        "debugMessage"
                )
                .containsExactly(
                        "Failed to add new product.",
                        HttpStatus.BAD_REQUEST,
                        String.format("'%s' product name is taken", name)
                );
    }

    @Test
    public void shouldThrowFailedToAddNewProductExceptionIfPriceIsNullable() {
        String name = "Apple";
        String description = "Fruit";
        Double price = null;
        String currency = "USD";

        assertThatThrownBy(() -> productService.addNewProduct(
                name,
                description,
                price,
                currency
        )).isInstanceOf(FailedToAddNewProductException.class)
                .extracting(
                        "message",
                        "status",
                        "debugMessage"
                )
                .containsExactly(
                        "Failed to add new product.",
                        HttpStatus.BAD_REQUEST,
                        "Price of new product can not be null or empty"
                );
    }

    @Test
    public void shouldThrowFailedToAddNewProductExceptionIfPriceIsLessThanZero() {
        String name = "Apple";
        String description = "Fruit";
        Double price = -1.12;
        String currency = "USD";

        assertThatThrownBy(() -> productService.addNewProduct(
                name,
                description,
                price,
                currency
        )).isInstanceOf(FailedToAddNewProductException.class)
                .extracting(
                        "message",
                        "status",
                        "debugMessage"
                )
                .containsExactly(
                        "Failed to add new product.",
                        HttpStatus.BAD_REQUEST,
                        "Price of new product can not be less than zero"
                );
    }

    @Test
    public void shouldThrowFailedToAddNewProductExceptionIfCurrencyIsNullable() {
        String name = "Apple";
        String description = "Fruit";
        Double price = 1.12;
        String currency = null;

        assertThatThrownBy(() -> productService.addNewProduct(
                name,
                description,
                price,
                currency
        )).isInstanceOf(FailedToAddNewProductException.class)
                .extracting(
                        "message",
                        "status",
                        "debugMessage"
                )
                .containsExactly(
                        "Failed to add new product.",
                        HttpStatus.BAD_REQUEST,
                        "Currency of new product can not be null or empty"
                );
    }

    @Test
    public void shouldThrowFailedToAddNewProductExceptionIfCurrencyIsEmpty() {
        String name = "Apple";
        String description = "Fruit";
        Double price = 1.12;
        String currency = "";

        assertThatThrownBy(() -> productService.addNewProduct(
                name,
                description,
                price,
                currency
        )).isInstanceOf(FailedToAddNewProductException.class)
                .extracting(
                        "message",
                        "status",
                        "debugMessage"
                )
                .containsExactly(
                        "Failed to add new product.",
                        HttpStatus.BAD_REQUEST,
                        "Currency of new product can not be null or empty"
                );
    }

    @Test
    public void shouldThrowFailedToAddNewProductExceptionIfCurrencyIsInvalid() {
        String name = "Apple";
        String description = "Fruit";
        Double price = 1.12;
        String currency = "EEE";

        assertThatThrownBy(() -> productService.addNewProduct(
                name,
                description,
                price,
                currency
        )).isInstanceOf(FailedToAddNewProductException.class)
                .extracting(
                        "message",
                        "status",
                        "debugMessage"
                )
                .containsExactly(
                        "Failed to add new product.",
                        HttpStatus.BAD_REQUEST,
                        String.format("Currency '%s' is invalid", currency)
                );
    }
}