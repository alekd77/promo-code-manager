package com.promocodes.promocodesmanager.promocode;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
class PromoCodeRepositoryTest {
    @Autowired
    private PromoCodeRepository promoCodeRepository;

    @AfterEach
    void tearDown() {
        promoCodeRepository.deleteAll();
    }

    @Test
    public void shouldSaveFixedAmountPromoCode() {
        PromoCode fixedPromoCode = new PromoCode(
                null,
                "SUMMER2024",
                LocalDate.now(),
                5,
                3,
                PromoCodeType.FIXED_AMOUNT,
                50.00,
                "USD",
                null
        );

        PromoCode savedPromoCode = promoCodeRepository.save(fixedPromoCode);

        assertThat(savedPromoCode)
                .usingRecursiveComparison()
                .ignoringFields("promoCodeId")
                .isEqualTo(fixedPromoCode);
    }

    @Test
    public void shouldSavePercentagePromoCode() {
        PromoCode percentagePromoCode = new PromoCode(
                null,
                "SUMMER2024",
                LocalDate.now(),
                5,
                3,
                PromoCodeType.PERCENTAGE,
                null,
                null,
                0.15
        );

        PromoCode savedPromoCode = promoCodeRepository.save(percentagePromoCode);

        assertThat(savedPromoCode)
                .usingRecursiveComparison()
                .ignoringFields("promoCodeId")
                .isEqualTo(percentagePromoCode);
    }

    @Test
    public void shouldFindAllPromoCodes() {
        PromoCode fixedPromoCode = new PromoCode(
                null,
                "SUMMER2024",
                LocalDate.now(),
                5,
                3,
                PromoCodeType.FIXED_AMOUNT,
                50.00,
                "USD",
                null
        );

        PromoCode percentagePromoCode = new PromoCode(
                null,
                "SUMMER2024",
                LocalDate.now(),
                5,
                3,
                PromoCodeType.PERCENTAGE,
                null,
                null,
                0.15
        );

        promoCodeRepository.save(fixedPromoCode);
        promoCodeRepository.save(percentagePromoCode);

        List<PromoCode> promoCodes = promoCodeRepository.findAll();

        assertThat(promoCodes)
                .hasSize(2)
                .contains(fixedPromoCode, percentagePromoCode);
    }

    @Test
    public void shouldReturnTrueIfPromoCodeExistsByText() {
        PromoCode fixedPromoCode = new PromoCode(
                null,
                "SUMMER2024",
                LocalDate.now(),
                5,
                3,
                PromoCodeType.FIXED_AMOUNT,
                50.00,
                "USD",
                null
        );

        PromoCode savedPromoCode = promoCodeRepository.save(fixedPromoCode);

        boolean exists = promoCodeRepository.existsByText("SUMMER2024");

        assertThat(exists).isTrue();
    }

    @Test
    public void shouldReturnFalseIfPromoCodeDoesNotExistByText() {
        PromoCode fixedPromoCode = new PromoCode(
                null,
                "SUMMER2024",
                LocalDate.now(),
                5,
                3,
                PromoCodeType.FIXED_AMOUNT,
                50.00,
                "USD",
                null
        );

        PromoCode savedPromoCode = promoCodeRepository.save(fixedPromoCode);

        boolean exists = promoCodeRepository.existsByText("SUMMER2023");

        assertThat(exists).isFalse();
    }

    @Test
    public void shouldFindProductByText() {
        PromoCode fixedPromoCode = new PromoCode(
                null,
                "SUMMER2024",
                LocalDate.now(),
                5,
                3,
                PromoCodeType.FIXED_AMOUNT,
                50.00,
                "USD",
                null
        );

        PromoCode savedPromoCode = promoCodeRepository.save(fixedPromoCode);

        Optional<PromoCode> promoCodeOptional = promoCodeRepository.findByText("SUMMER2024");

        assertThat(promoCodeOptional).isPresent();
        assertThat(promoCodeOptional.get())
                .usingRecursiveComparison()
                .ignoringFields("promoCodeId")
                .isEqualTo(fixedPromoCode);
    }

    @Test
    public void shouldReturnEmptyOptionalIfProductDoesNotExistByText() {
        PromoCode fixedPromoCode = new PromoCode(
                null,
                "SUMMER2024",
                LocalDate.now(),
                5,
                3,
                PromoCodeType.FIXED_AMOUNT,
                50.00,
                "USD",
                null
        );

        PromoCode savedPromoCode = promoCodeRepository.save(fixedPromoCode);

        Optional<PromoCode> promoCodeOptional = promoCodeRepository.findByText("SUMMER2023");

        assertThat(promoCodeOptional).isEmpty();
    }
}