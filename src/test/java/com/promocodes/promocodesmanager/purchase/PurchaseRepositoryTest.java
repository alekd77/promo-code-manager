package com.promocodes.promocodesmanager.purchase;

import com.promocodes.promocodesmanager.product.ProductRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class PurchaseRepositoryTest {
    private final PurchaseRepository purchaseRepository;
    private final ProductRepository productRepository;

    @Autowired
    public PurchaseRepositoryTest(PurchaseRepository purchaseRepository,
                                  ProductRepository productRepository) {
        this.purchaseRepository = purchaseRepository;
        this.productRepository = productRepository;
    }

    @AfterEach
    void tearDown() {
        purchaseRepository.deleteAll();
    }

    @Test
    public void shouldSavePurchase() {
        Purchase purchase = new Purchase(
                null,
                "NikeShoes",
                LocalDate.now().minusDays(5),
                155.55,
                100.0,
                "USD"
        );

        Purchase savedPurchase = purchaseRepository.save(purchase);

        assertThat(savedPurchase.getPurchaseId())
                .isNotNull();
        assertThat(savedPurchase)
                .usingRecursiveComparison()
                .ignoringFields("purchaseId")
                .isEqualTo(purchase);
    }

    @Test
    public void shouldFindAllPurchases() {
        Purchase purchase1 = new Purchase(
                null,
                "NikeShoes",
                LocalDate.now().minusDays(5),
                155.55,
                100.0,
                "USD"
        );

        Purchase purchase2 = new Purchase(
                null,
                "NikeShoes",
                LocalDate.now().minusDays(5),
                155.55,
                80.0,
                "USD"
        );

        Purchase purchase3 = new Purchase(
                null,
                "NikeShoes",
                LocalDate.now().minusDays(5),
                155.55,
                70.0,
                "USD"
        );

        List<Purchase> savedPurchases = new ArrayList<>();
        savedPurchases.add(
                purchaseRepository.save(purchase1)
        );
        savedPurchases.add(
                purchaseRepository.save(purchase2)
        );
        savedPurchases.add(
                purchaseRepository.save(purchase3)
        );

        List<Purchase> foundPurchases = purchaseRepository.findAll();

        assertThat(foundPurchases)
                .isNotNull();
        assertThat(savedPurchases.size())
                .isEqualTo(foundPurchases.size());
        assertThat(savedPurchases)
                .containsExactlyInAnyOrderElementsOf(foundPurchases);
    }

    @Test
    public void shouldReturnEmptyListIfNoPurchaseIsFound() {
        List<Purchase> foundPurchases = purchaseRepository.findAll();

        assertThat(foundPurchases)
               .isNotNull();
        assertThat(foundPurchases.size())
               .isEqualTo(0);
        assertThat(foundPurchases)
                .isEqualTo(Collections.emptyList());
    }

}