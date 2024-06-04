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
import java.util.*;

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
    public void shouldReturnsSinglePurchaseGroupForSameCurrencyWithThreeElements() {
        List<Purchase> purchases = new ArrayList<>();
        purchases.add(
                new Purchase(
                        46L,
                        "NikeShoes",
                        LocalDate.now().minusDays(30),
                        150.0,
                        100.0,
                        "USD"
                )
        );

        purchases.add(
                new Purchase(
                        48L,
                        "NikeShoes",
                        LocalDate.now().minusDays(10),
                        150.0,
                        80.0,
                        "USD"
                )
        );

        purchases.add(
                new Purchase(
                        49L,
                        "NikeShoes",
                        LocalDate.now().minusDays(8),
                        150.0,
                        75.0,
                        "USD"
                )
        );

        when(purchaseRepository.findAll()).thenReturn(purchases);

        List<List<Purchase>> purchasesGroupedByCurrency =
                purchaseService.findPurchasesGroupedByCurrency();

        verify(purchaseRepository, times(1))
                .findAll();

        assertThat(1).isEqualTo(purchasesGroupedByCurrency.size());
        assertThat(3).isEqualTo(purchasesGroupedByCurrency.get(0).size());
    }

    @Test
    public void shouldReturnsSinglePurchaseGroupForSameCurrencyWithFiveElements() {
        List<Purchase> purchases = new ArrayList<>();
        purchases.add(
                new Purchase(
                        46L,
                        "NikeShoes",
                        LocalDate.now().minusDays(30),
                        150.0,
                        100.0,
                        "USD"
                )
        );

        purchases.add(
                new Purchase(
                        48L,
                        "NikeShoes",
                        LocalDate.now().minusDays(10),
                        150.0,
                        80.0,
                        "USD"
                )
        );

        purchases.add(
                new Purchase(
                        49L,
                        "NikeShoes",
                        LocalDate.now().minusDays(8),
                        150.0,
                        75.0,
                        "USD"
                )
        );

        purchases.add(
                new Purchase(
                        50L,
                        "NikeShoes",
                        LocalDate.now().minusDays(6),
                        150.0,
                        32.0,
                        "USD"
                )
        );

        purchases.add(
                new Purchase(
                        60L,
                        "NikeShoes",
                        LocalDate.now().minusDays(6),
                        150.0,
                        32.0,
                        "USD"
                )
        );

        when(purchaseRepository.findAll()).thenReturn(purchases);

        List<List<Purchase>> purchasesGroupedByCurrency =
                purchaseService.findPurchasesGroupedByCurrency();

        verify(purchaseRepository, times(1))
                .findAll();

        assertThat(1).isEqualTo(purchasesGroupedByCurrency.size());
        assertThat(5).isEqualTo(purchasesGroupedByCurrency.get(0).size());
    }

    @Test
    public void shouldReturnsEmptyPurchaseGroupForSameCurrency() {
        when(purchaseRepository.findAll()).thenReturn(Collections.emptyList());

        List<List<Purchase>> purchasesGroupedByCurrency =
                purchaseService.findPurchasesGroupedByCurrency();

        verify(purchaseRepository, times(1))
                .findAll();

        assertThat(0).isEqualTo(purchasesGroupedByCurrency.size());
    }

    @Test
    public void shouldReturnsThreePurchaseGroupForSameCurrencyWithOneElementEach() {
        List<Purchase> purchases = new ArrayList<>();
        purchases.add(
                new Purchase(
                        46L,
                        "NikeShoes",
                        LocalDate.now().minusDays(30),
                        150.0,
                        100.0,
                        "USD"
                )
        );

        purchases.add(
                new Purchase(
                        48L,
                        "AdidasShoes",
                        LocalDate.now().minusDays(10),
                        200.0,
                        80.0,
                        "EUR"
                )
        );

        purchases.add(
                new Purchase(
                        49L,
                        "PumaShoes",
                        LocalDate.now().minusDays(8),
                        343.0,
                        75.0,
                        "PLN"
                )
        );

        when(purchaseRepository.findAll()).thenReturn(purchases);

        List<List<Purchase>> purchasesGroupedByCurrency =
                purchaseService.findPurchasesGroupedByCurrency();

        verify(purchaseRepository, times(1))
                .findAll();

        assertThat(3).isEqualTo(purchasesGroupedByCurrency.size());
        assertThat(1).isEqualTo(purchasesGroupedByCurrency.get(0).size());
        assertThat(1).isEqualTo(purchasesGroupedByCurrency.get(1).size());
        assertThat(1).isEqualTo(purchasesGroupedByCurrency.get(2).size());

        Set<String> currencies = new HashSet<>();
        currencies.add("USD");
        currencies.add("EUR");
        currencies.add("PLN");

        for (List<Purchase> purchaseList : purchasesGroupedByCurrency) {
            String purchaseCurrency = purchaseList.get(0).getCurrency();
            assertThat(currencies).contains(purchaseCurrency);
            currencies.remove(purchaseCurrency);
        }

        assertThat(0).isEqualTo(currencies.size());
    }

    @Test
    public void shouldReturnsTwoPurchaseGroupForSameCurrency() {
        List<Purchase> purchases = new ArrayList<>();
        purchases.add(
                new Purchase(
                        46L,
                        "NikeShoes",
                        LocalDate.now().minusDays(30),
                        150.0,
                        100.0,
                        "USD"
                )
        );

        purchases.add(
                new Purchase(
                        48L,
                        "AdidasShoes",
                        LocalDate.now().minusDays(10),
                        200.0,
                        80.0,
                        "USD"
                )
        );

        purchases.add(
                new Purchase(
                        49L,
                        "PumaShoes",
                        LocalDate.now().minusDays(8),
                        343.0,
                        75.0,
                        "PLN"
                )
        );

        when(purchaseRepository.findAll()).thenReturn(purchases);

        List<List<Purchase>> purchasesGroupedByCurrency =
                purchaseService.findPurchasesGroupedByCurrency();

        verify(purchaseRepository, times(1))
                .findAll();

        assertThat(2).isEqualTo(purchasesGroupedByCurrency.size());
        Set<Integer> groupSizes = new HashSet<>();
        groupSizes.add(2);
        groupSizes.add(1);

        Set<String> currencies = new HashSet<>();
        currencies.add("USD");
        currencies.add("PLN");

        for (List<Purchase> purchaseList : purchasesGroupedByCurrency) {
            assertThat(groupSizes).contains(purchaseList.size());
            groupSizes.remove(purchaseList.size());

            assertThat(currencies).contains(purchaseList.get(0).getCurrency());
            currencies.remove(purchaseList.get(0).getCurrency());
        }

        assertThat(0).isEqualTo(groupSizes.size());
        assertThat(0).isEqualTo(currencies.size());
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
        assertThat(product.getName())
                .isEqualTo(captor.getValue().getProductName());
        assertThat(captor.getValue().getPurchaseDate())
                .isNotNull();
        assertThat(product.getPrice())
                .isEqualTo(captor.getValue().getRegularPrice());
        assertThat(50.0)
                .isEqualTo(captor.getValue().getDiscountAmount());
        assertThat(product.getCurrency())
                .isEqualTo(captor.getValue().getCurrency());
    }
}