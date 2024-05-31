package com.promocodes.promocodesmanager.purchase;

import com.promocodes.promocodesmanager.product.Product;
import com.promocodes.promocodesmanager.product.ProductRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

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

}