package com.promocodes.promocodesmanager.purchase;

import com.promocodes.promocodesmanager.product.Product;
import com.promocodes.promocodesmanager.product.ProductService;
import com.promocodes.promocodesmanager.promocode.PromoCode;
import com.promocodes.promocodesmanager.promocode.PromoCodeService;
import com.promocodes.promocodesmanager.promocode.PromoCodeType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PurchaseServiceTest {
    @Mock
    private PurchaseRepository purchaseRepository;

    @Mock
    private ProductService productService;

    @Mock
    private PromoCodeService promoCodeService;

    private PurchaseService purchaseService;

    @BeforeEach
    void setUp() {
        purchaseService = new PurchaseService(
                purchaseRepository,
                productService,
                promoCodeService
        );
    }

    @Test
    public void shouldHandlePurchase() {
        Product product = new Product(
                123L,
                "NikeShoes",
                "Clothes",
                150.0,
                "USD"
        );

        when(productService.findProductByName("NikeShoes"))
                .thenReturn(product);

        when(productService.calculateDiscountProductPrice(
                "NikeShoes",
                "SUMMER2024")
        ).thenReturn(100.0);

        purchaseService.handlePurchase(
                "NikeShoes",
                "SUMMER2024"
        );

        ArgumentCaptor<Purchase> captor =
                ArgumentCaptor.forClass(Purchase.class);

        verify(purchaseRepository, times(1))
                .save(captor.capture());

        assertThat(captor.getValue())
                .isNotNull();
        assertThat(captor.getValue().getProduct())
                .isNotNull();
        assertThat(captor.getValue().getPurchaseDate())
                .isNotNull();
        assertThat(product.getPrice())
                .isEqualTo(captor.getValue().getRegularPrice());
        assertThat(100.0)
                .isEqualTo(captor.getValue().getDiscountAmount());
        assertThat(product.getCurrency())
                .isEqualTo(captor.getValue().getCurrency());
    }
}