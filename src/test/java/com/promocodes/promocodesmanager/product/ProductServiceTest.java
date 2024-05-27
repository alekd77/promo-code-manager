package com.promocodes.promocodesmanager.product;

import com.promocodes.promocodesmanager.exception.*;
import com.promocodes.promocodesmanager.promocode.PromoCode;
import com.promocodes.promocodesmanager.promocode.PromoCodeService;
import com.promocodes.promocodesmanager.promocode.PromoCodeType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
    @Mock
    private ProductRepository productRepository;
    @Mock
    private PromoCodeService promoCodeService;
    private ProductService productService;

    @BeforeEach
    void setUp() {
        productService = new ProductService(
                productRepository,
                promoCodeService
        );
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

    @Test
    public void shouldUpdateProduct() {
        when(productRepository.findByName("Apple"))
                .thenReturn(Optional.of(new Product(
                        "Apple",
                        "Fruit",
                        1.12,
                        "USD"))
                );
        when(productRepository.existsByName("Gold Apple")).thenReturn(false);

        productService.updateProduct(
                "Apple",
                "Gold Apple",
                "",
                1.22,
                "USD"
        );

        ArgumentCaptor<Product> productArgumentCaptor =
                ArgumentCaptor.forClass(Product.class);

        verify(productRepository, times(1))
                .save(productArgumentCaptor.capture());

        assertThat(productArgumentCaptor.getValue().getName())
                .isEqualTo("Gold Apple");
        assertThat(productArgumentCaptor.getValue().getDescription())
                .isEqualTo("");
        assertThat(productArgumentCaptor.getValue().getPrice())
                .isEqualTo(1.22);
        assertThat(productArgumentCaptor.getValue().getCurrency())
                .isEqualTo("USD");
    }

    @Test
    public void shouldThrowProductNotFoundExceptionIfProductNameIsNullable() {
        assertThatThrownBy(() -> productService.updateProduct(
                null,
                "Gold Apple",
                "",
                1.22,
                "USD"
        )).isInstanceOf(ProductNotFoundException.class)
                .extracting(
                        "message",
                        "status",
                        "debugMessage"
                )
                .containsExactly(
                        "Product not found.",
                        HttpStatus.NOT_FOUND,
                        "Product name is null or empty."
                );
    }

    @Test
    public void shouldThrowProductNotFoundExceptionIfProductDoesNotExist() {
        when(productRepository.findByName("Apple"))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> productService.updateProduct(
                "Apple",
                "Gold Apple",
                "",
                1.22,
                "USD"
        )).isInstanceOf(ProductNotFoundException.class)
                .extracting(
                        "message",
                        "status",
                        "debugMessage"
                )
                .containsExactly(
                        "Product not found.",
                        HttpStatus.NOT_FOUND,
                        ""
                );
    }


    @Test
    public void shouldThrowFailedToUpdateProductExceptionIfProductOfGivenNameAlreadyExists() {
        when(productRepository.findByName("Apple"))
                .thenReturn(Optional.of(new Product(
                        "Apple",
                        "Fruit",
                        1.12,
                        "USD"))
                );
        when(productRepository.existsByName("Gold Apple")).thenReturn(true);

        assertThatThrownBy(() -> productService.updateProduct(
                "Apple",
                "Gold Apple",
                "",
                1.22,
                "USD"
        )).isInstanceOf(FailedToUpdateProductException.class)
                .extracting(
                        "message",
                        "status",
                        "debugMessage"
                )
                .containsExactly(
                        "Failed to update product.",
                        HttpStatus.BAD_REQUEST,
                        "Product 'Gold Apple' already exists."
                );
    }

    @Test
    public void shouldThrowFailedToUpdateProductExceptionIfNewPriceIsLessThanZero() {
        when(productRepository.findByName("Apple"))
                .thenReturn(Optional.of(new Product(
                        "Apple",
                        "Fruit",
                        1.12,
                        "USD"))
                );
        when(productRepository.existsByName("Gold Apple")).thenReturn(false);

        assertThatThrownBy(() -> productService.updateProduct(
                "Apple",
                "Gold Apple",
                "",
                -1.22,
                "USD"
        )).isInstanceOf(FailedToUpdateProductException.class)
                .extracting(
                        "message",
                        "status",
                        "debugMessage"
                )
                .containsExactly(
                        "Failed to update product.",
                        HttpStatus.BAD_REQUEST,
                        "Price of the product can not be less than zero."
                );
    }

    @Test
    public void shouldThrowFailedToUpdateProductExceptionIfNewCurrencyIsInvalid() {
        when(productRepository.findByName("Apple"))
                .thenReturn(Optional.of(new Product(
                        "Apple",
                        "Fruit",
                        1.12,
                        "USD"))
                );
        when(productRepository.existsByName("Gold Apple")).thenReturn(false);

        assertThatThrownBy(() -> productService.updateProduct(
                "Apple",
                "Gold Apple",
                "",
                1.22,
                "xxxx"
        )).isInstanceOf(FailedToUpdateProductException.class)
                .extracting(
                        "message",
                        "status",
                        "debugMessage"
                )
                .containsExactly(
                        "Failed to update product.",
                        HttpStatus.BAD_REQUEST,
                        "Currency 'xxxx' is not valid."
                );
    }

    @Test
    public void shouldReturnDiscountProductPriceGreaterThanZeroIfFixedAmountDiscountLessThanProductPrice() {
        Product product = new Product(
                123L,
                "Nike Shoes",
                "Clothes",
                150.00,
                "USD"
        );

        PromoCode promoCode = new PromoCode(
                244L,
                "SUMMER2024",
                LocalDate.now().plusDays(30),
                5,
                3,
                PromoCodeType.FIXED_AMOUNT,
                50.00,
                "USD",
                null
        );
        when(productRepository.findByName("Nike Shoes"))
                .thenReturn(Optional.of(product));

        when(promoCodeService.getPromoCodeByText("SUMMER2024"))
                .thenReturn(promoCode);

        Double result = productService.calculateDiscountProductPrice(
                "Nike Shoes",
                "SUMMER2024"
        );

        assertThat(100.0).isEqualTo(result);
    }

    @Test
    public void shouldReturnDiscountProductPriceEqualZeroIfFixedAmountDiscountGreaterThanProductPrice() {
        Product product = new Product(
                123L,
                "Nike Shoes",
                "Clothes",
                150.00,
                "USD"
        );

        PromoCode promoCode = new PromoCode(
                244L,
                "SUMMER2024",
                LocalDate.now().plusDays(30),
                5,
                3,
                PromoCodeType.FIXED_AMOUNT,
                500.00,
                "USD",
                null
        );
        when(productRepository.findByName("Nike Shoes"))
                .thenReturn(Optional.of(product));

        when(promoCodeService.getPromoCodeByText("SUMMER2024"))
                .thenReturn(promoCode);

        Double result = productService.calculateDiscountProductPrice(
                "Nike Shoes",
                "SUMMER2024"
        );

        assertThat(0.0).isEqualTo(result);
    }

    @Test
    public void shouldReturnDiscountProductPriceEqualZeroIfFixedAmountDiscountEqualProductPrice() {
        Product product = new Product(
                123L,
                "Nike Shoes",
                "Clothes",
                150.00,
                "USD"
        );

        PromoCode promoCode = new PromoCode(
                244L,
                "SUMMER2024",
                LocalDate.now().plusDays(30),
                5,
                3,
                PromoCodeType.FIXED_AMOUNT,
                150.00,
                "USD",
                null
        );
        when(productRepository.findByName("Nike Shoes"))
                .thenReturn(Optional.of(product));

        when(promoCodeService.getPromoCodeByText("SUMMER2024"))
                .thenReturn(promoCode);

        Double result = productService.calculateDiscountProductPrice(
                "Nike Shoes",
                "SUMMER2024"
        );

        assertThat(0.0).isEqualTo(result);
    }

    @Test
    public void shouldReturnDiscountProductPriceGreaterThanZeroIfPercentageDiscountLessThanProductPrice() {
        Product product = new Product(
                123L,
                "Nike Shoes",
                "Clothes",
                150.00,
                "USD"
        );

        PromoCode promoCode = new PromoCode(
                244L,
                "LOL",
                LocalDate.now().plusDays(30),
                5,
                3,
                PromoCodeType.PERCENTAGE,
                null,
                null,
                90
        );
        when(productRepository.findByName("Nike Shoes"))
                .thenReturn(Optional.of(product));

        when(promoCodeService.getPromoCodeByText("SUMMER2024"))
                .thenReturn(promoCode);

        Double result = productService.calculateDiscountProductPrice(
                "Nike Shoes",
                "SUMMER2024"
        );

        assertThat(15.0).isEqualTo(result);
    }

    @Test
    public void shouldReturnDiscountProductPriceEqualsZeroIfPercentageDiscountEqualsMax() {
        Product product = new Product(
                123L,
                "Nike Shoes",
                "Clothes",
                150.00,
                "USD"
        );

        PromoCode promoCode = new PromoCode(
                244L,
                "LOL",
                LocalDate.now().plusDays(30),
                5,
                3,
                PromoCodeType.PERCENTAGE,
                null,
                null,
                100
        );
        when(productRepository.findByName("Nike Shoes"))
                .thenReturn(Optional.of(product));

        when(promoCodeService.getPromoCodeByText("SUMMER2024"))
                .thenReturn(promoCode);

        Double result = productService.calculateDiscountProductPrice(
                "Nike Shoes",
                "SUMMER2024"
        );

        assertThat(0.0).isEqualTo(result);
    }

    @Test
    public void shouldReturnDiscountProductPriceIfPromoCodeExpDateIsToday() {
        Product product = new Product(
                123L,
                "Nike Shoes",
                "Clothes",
                150.00,
                "USD"
        );

        PromoCode promoCode = new PromoCode(
                244L,
                "SUMMER2024",
                LocalDate.now(),
                5,
                3,
                PromoCodeType.FIXED_AMOUNT,
                50.00,
                "USD",
                null
        );
        when(productRepository.findByName("Nike Shoes"))
                .thenReturn(Optional.of(product));

        when(promoCodeService.getPromoCodeByText("SUMMER2024"))
                .thenReturn(promoCode);

        Double result = productService.calculateDiscountProductPrice(
                "Nike Shoes",
                "SUMMER2024"
        );

        assertThat(100.0).isEqualTo(result);
    }

    @Test
    public void shouldThrowProductDiscountCalculationExceptionIfPromoCodeIsExpired() {
        Product product = new Product(
                123L,
                "Nike Shoes",
                "Clothes",
                150.00,
                "USD"
        );

        PromoCode promoCode = new PromoCode(
                244L,
                "SUMMER2024",
                LocalDate.now().minusDays(1),
                5,
                3,
                PromoCodeType.FIXED_AMOUNT,
                50.00,
                "USD",
                null
        );
        when(productRepository.findByName("Nike Shoes"))
                .thenReturn(Optional.of(product));

        when(promoCodeService.getPromoCodeByText("SUMMER2024"))
                .thenReturn(promoCode);

        assertThatThrownBy(() -> productService.calculateDiscountProductPrice(
                "Nike Shoes",
                "SUMMER2024"
        )).isInstanceOf(ProductDiscountCalculationException.class)
                .extracting(
                        "message",
                        "status",
                        "debugMessage"
                )
                .containsExactly(
                        "Product discount calculation failed.",
                        HttpStatus.BAD_REQUEST,
                        "Promo code is expired."
                );
    }

    @Test
    public void shouldThrowProductDiscountCalculationExceptionIfPromoCodeIsUsedUp() {
        Product product = new Product(
                123L,
                "Nike Shoes",
                "Clothes",
                150.00,
                "USD"
        );

        PromoCode promoCode = new PromoCode(
                244L,
                "SUMMER2024",
                LocalDate.now().plusDays(30),
                5,
                0,
                PromoCodeType.FIXED_AMOUNT,
                50.00,
                "USD",
                null
        );
        when(productRepository.findByName("Nike Shoes"))
                .thenReturn(Optional.of(product));

        when(promoCodeService.getPromoCodeByText("SUMMER2024"))
                .thenReturn(promoCode);

        assertThatThrownBy(() -> productService.calculateDiscountProductPrice(
                "Nike Shoes",
                "SUMMER2024"
        )).isInstanceOf(ProductDiscountCalculationException.class)
                .extracting(
                        "message",
                        "status",
                        "debugMessage"
                )
                .containsExactly(
                        "Product discount calculation failed.",
                        HttpStatus.BAD_REQUEST,
                        "Promo code is used up."
                );
    }

    @Test
    public void shouldThrowProductDiscountCalculationExceptionIfCurrenciesDoNotMatch() {
        Product product = new Product(
                123L,
                "Nike Shoes",
                "Clothes",
                150.00,
                "USD"
        );

        PromoCode promoCode = new PromoCode(
                244L,
                "SUMMER2024",
                LocalDate.now().plusDays(30),
                5,
                5,
                PromoCodeType.FIXED_AMOUNT,
                50.00,
                "EUR",
                null
        );
        when(productRepository.findByName("Nike Shoes"))
                .thenReturn(Optional.of(product));

        when(promoCodeService.getPromoCodeByText("SUMMER2024"))
                .thenReturn(promoCode);

        assertThatThrownBy(() -> productService.calculateDiscountProductPrice(
                "Nike Shoes",
                "SUMMER2024"
        )).isInstanceOf(ProductDiscountCalculationException.class)
                .extracting(
                        "message",
                        "status",
                        "debugMessage"
                )
                .containsExactly(
                        "Product discount calculation failed.",
                        HttpStatus.BAD_REQUEST,
                        String.format(
                                "Currencies of product '%s' " +
                                        "and promo code '%s' do not match.",
                                product.getName(),
                                promoCode.getText()
                        )
                );
    }

    @Test
    public void shouldThrowProductNotFoundException() {
        when(productRepository.findByName("Nike Shoes"))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> productService.calculateDiscountProductPrice(
                "Nike Shoes",
                "SUMMER2024")
        ).isInstanceOf(ProductNotFoundException.class)
                .extracting(
                        "message",
                        "status",
                        "debugMessage"
                )
                .containsExactly(
                        "Product not found.",
                        HttpStatus.NOT_FOUND,
                        ""
                );
    }

    @Test
    public void shouldThrowPromoCodeNotFoundException() {
        Product product = new Product(
                123L,
                "Nike Shoes",
                "Clothes",
                150.00,
                "USD"
        );

        when(productRepository.findByName("Nike Shoes"))
                .thenReturn(Optional.of(product));
        when(promoCodeService.getPromoCodeByText("SUMMER2024"))
                .thenThrow(new PromoCodeNotFoundException());

        assertThatThrownBy(() -> productService.calculateDiscountProductPrice(
                "Nike Shoes",
                "SUMMER2024")
        ).isInstanceOf(PromoCodeNotFoundException.class)
                .extracting(
                        "message",
                        "status",
                        "debugMessage"
                )
                .containsExactly(
                        "Promo code not found.",
                        HttpStatus.NOT_FOUND,
                        ""
                );
    }
}