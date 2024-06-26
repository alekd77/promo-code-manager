package com.promocodes.promocodesmanager.promocode;

import com.promocodes.promocodesmanager.exception.FailedToAddNewPromoCodeException;
import com.promocodes.promocodesmanager.exception.PromoCodeNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PromoCodeServiceTest {
    @Mock
    private PromoCodeRepository promoCodeRepository;

    private PromoCodeService promoCodeService;

    @BeforeEach
    void setUp() {
        promoCodeService = new PromoCodeService(promoCodeRepository);
    }

    @Test
    public void shouldReturnAllPromoCodes() {
        promoCodeService.getAllPromoCodes();

        verify(promoCodeRepository, times(1))
                .findAll();
    }

    @Test
    public void shouldReturnPromoCodeByText() {
        String text = "TEST123";

        PromoCode promoCode = new PromoCode(
                123L,
                "TEST123",
                LocalDate.of(2024, 8, 18),
                10,
                10,
                PromoCodeType.FIXED_AMOUNT,
                55.55,
                "USD",
                null
        );

        when(promoCodeRepository.findByText(text))
                .thenReturn(Optional.of(promoCode));

        promoCodeService.getPromoCodeByText(text);

        verify(promoCodeRepository, times(1))
               .findByText(text);
    }

    @Test
    public void shouldThrowPromoCodeNotFoundIfPromoCodeDoesNotExistByText() {
        String text = "TEST123";

        when(promoCodeRepository.findByText(text))
               .thenReturn(Optional.empty());

        assertThatThrownBy(() -> promoCodeService.getPromoCodeByText(text))
                .isInstanceOf(PromoCodeNotFoundException.class)
                .extracting(
                        "message",
                        "status",
                        "debugMessage"
                ).containsExactly(
                        "Promo code not found.",
                        HttpStatus.NOT_FOUND,
                        ""
                );
    }

    @Test
    public void shouldAddNewFixedAmountPromoCode() {
        String text = "TEST123";
        LocalDate expirationDate = LocalDate.of(2024, 8, 18);
        Integer usagesAllowed = 10;
        Double discountAmount = 55.55;
        String discountCurrency = "USD";

        when(promoCodeRepository.existsByText(text)).thenReturn(false);

        promoCodeService.addNewFixedAmountPromoCode(
                text,
                expirationDate,
                usagesAllowed,
                discountAmount,
                discountCurrency
        );

        ArgumentCaptor<PromoCode> captor =
                ArgumentCaptor.forClass(PromoCode.class);

        verify(promoCodeRepository, times(1))
               .save(captor.capture());

        PromoCode val = captor.getValue();

        assertThat(text).isEqualTo(val.getText());
        assertThat(expirationDate).isEqualTo(val.getExpirationDate());
        assertThat(usagesAllowed).isEqualTo(val.getUsagesTotal());
        assertThat(usagesAllowed).isEqualTo(val.getUsagesLeft());
        assertThat(discountAmount).isEqualTo(val.getDiscountAmount());
        assertThat(discountCurrency).isEqualTo(val.getDiscountCurrency());
        assertThat(val.getType()).isEqualTo(PromoCodeType.FIXED_AMOUNT);
        assertThat(val.getDiscountPercentage()).isNull();
    }

    @Test
    public void shouldThrowFailedToAddNewPromoCodeExceptionIfTextIsNull() {
        String text = null;
        LocalDate expirationDate = LocalDate.of(2024, 8, 18);
        Integer usagesAllowed = 10;
        Double discountAmount = 55.55;
        String discountCurrency = "USD";

        assertThatThrownBy(() -> promoCodeService.addNewFixedAmountPromoCode(
                text,
                expirationDate,
                usagesAllowed,
                discountAmount,
                discountCurrency
        )).isInstanceOf(FailedToAddNewPromoCodeException.class)
               .extracting(
                        "message",
                        "status",
                        "debugMessage"
                ).containsExactly(
                        "Failed to add new promo code.",
                        HttpStatus.BAD_REQUEST,
                        "Promo code text can not be null or empty."
                );

        verify(promoCodeRepository, times(0))
               .save(any());
    }

    @Test
    public void shouldThrowFailedToAddNewPromoCodeExceptionIfTextIsEmpty() {
        String text = "";
        LocalDate expirationDate = LocalDate.of(2024, 8, 18);
        Integer usagesAllowed = 10;
        Double discountAmount = 55.55;
        String discountCurrency = "USD";

        assertThatThrownBy(() -> promoCodeService.addNewFixedAmountPromoCode(
                text,
                expirationDate,
                usagesAllowed,
                discountAmount,
                discountCurrency
        )).isInstanceOf(FailedToAddNewPromoCodeException.class)
                .extracting(
                        "message",
                        "status",
                        "debugMessage"
                ).containsExactly(
                        "Failed to add new promo code.",
                        HttpStatus.BAD_REQUEST,
                        "Promo code text can not be null or empty."
                );

        verify(promoCodeRepository, times(0))
                .save(any());
    }

    @Test
    public void shouldThrowFailedToAddNewPromoCodeExceptionIfTextLengthIs2() {
        String text = "TE";
        LocalDate expirationDate = LocalDate.of(2024, 8, 18);
        Integer usagesAllowed = 10;
        Double discountAmount = 55.55;
        String discountCurrency = "USD";

        assertThatThrownBy(() -> promoCodeService.addNewFixedAmountPromoCode(
                text,
                expirationDate,
                usagesAllowed,
                discountAmount,
                discountCurrency
        )).isInstanceOf(FailedToAddNewPromoCodeException.class)
                .extracting(
                        "message",
                        "status",
                        "debugMessage"
                ).containsExactly(
                        "Failed to add new promo code.",
                        HttpStatus.BAD_REQUEST,
                        "Promo code text must be " +
                                "between 3 and 24 characters long."
                );

        verify(promoCodeRepository, times(0))
                .save(any());
    }

    @Test
    public void shouldThrowFailedToAddNewPromoCodeExceptionIfTextLengthIs27() {
        String text = "TTTTTTTTTTTTTTAATATATATATAT";
        LocalDate expirationDate = LocalDate.of(2024, 8, 18);
        Integer usagesAllowed = 10;
        Double discountAmount = 55.55;
        String discountCurrency = "USD";

        assertThatThrownBy(() -> promoCodeService.addNewFixedAmountPromoCode(
                text,
                expirationDate,
                usagesAllowed,
                discountAmount,
                discountCurrency
        )).isInstanceOf(FailedToAddNewPromoCodeException.class)
                .extracting(
                        "message",
                        "status",
                        "debugMessage"
                ).containsExactly(
                        "Failed to add new promo code.",
                        HttpStatus.BAD_REQUEST,
                        "Promo code text must be " +
                                "between 3 and 24 characters long."
                );

        verify(promoCodeRepository, times(0))
                .save(any());
    }

    @Test
    public void shouldThrowFailedToAddNewPromoCodeExceptionIfTextContainsWhitespaceAtTheBeginning() {
        String text = " TEST123";
        LocalDate expirationDate = LocalDate.of(2024, 8, 18);
        Integer usagesAllowed = 10;
        Double discountAmount = 55.55;
        String discountCurrency = "USD";

        assertThatThrownBy(() -> promoCodeService.addNewFixedAmountPromoCode(
                text,
                expirationDate,
                usagesAllowed,
                discountAmount,
                discountCurrency
        )).isInstanceOf(FailedToAddNewPromoCodeException.class)
                .extracting(
                        "message",
                        "status",
                        "debugMessage"
                ).containsExactly(
                        "Failed to add new promo code.",
                        HttpStatus.BAD_REQUEST,
                        "Promo code text must only contain " +
                                "letters (a-z, A-Z) and digits (0-9)."
                );

        verify(promoCodeRepository, times(0))
                .save(any());
    }

    @Test
    public void shouldThrowFailedToAddNewPromoCodeExceptionIfTextContainsWhitespaceInTheMiddle() {
        String text = "TEST 123";
        LocalDate expirationDate = LocalDate.of(2024, 8, 18);
        Integer usagesAllowed = 10;
        Double discountAmount = 55.55;
        String discountCurrency = "USD";

        assertThatThrownBy(() -> promoCodeService.addNewFixedAmountPromoCode(
                text,
                expirationDate,
                usagesAllowed,
                discountAmount,
                discountCurrency
        )).isInstanceOf(FailedToAddNewPromoCodeException.class)
                .extracting(
                        "message",
                        "status",
                        "debugMessage"
                ).containsExactly(
                        "Failed to add new promo code.",
                        HttpStatus.BAD_REQUEST,
                        "Promo code text must only contain " +
                                "letters (a-z, A-Z) and digits (0-9)."
                );

        verify(promoCodeRepository, times(0))
                .save(any());
    }

    @Test
    public void shouldThrowFailedToAddNewPromoCodeExceptionIfTextContainsWhitespaceAtTheEnd() {
        String text = "TEST123 ";
        LocalDate expirationDate = LocalDate.of(2024, 8, 18);
        Integer usagesAllowed = 10;
        Double discountAmount = 55.55;
        String discountCurrency = "USD";

        assertThatThrownBy(() -> promoCodeService.addNewFixedAmountPromoCode(
                text,
                expirationDate,
                usagesAllowed,
                discountAmount,
                discountCurrency
        )).isInstanceOf(FailedToAddNewPromoCodeException.class)
                .extracting(
                        "message",
                        "status",
                        "debugMessage"
                ).containsExactly(
                        "Failed to add new promo code.",
                        HttpStatus.BAD_REQUEST,
                        "Promo code text must only contain " +
                                "letters (a-z, A-Z) and digits (0-9)."
                );

        verify(promoCodeRepository, times(0))
                .save(any());
    }

    @Test
    public void shouldThrowFailedToAddNewPromoCodeExceptionIfTextContainsANCharacter1() {
        String text = "TEST123!";
        LocalDate expirationDate = LocalDate.of(2024, 8, 18);
        Integer usagesAllowed = 10;
        Double discountAmount = 55.55;
        String discountCurrency = "USD";

        assertThatThrownBy(() -> promoCodeService.addNewFixedAmountPromoCode(
                text,
                expirationDate,
                usagesAllowed,
                discountAmount,
                discountCurrency
        )).isInstanceOf(FailedToAddNewPromoCodeException.class)
                .extracting(
                        "message",
                        "status",
                        "debugMessage"
                ).containsExactly(
                        "Failed to add new promo code.",
                        HttpStatus.BAD_REQUEST,
                        "Promo code text must only contain " +
                                "letters (a-z, A-Z) and digits (0-9)."
                );

        verify(promoCodeRepository, times(0))
                .save(any());
    }

    @Test
    public void shouldThrowFailedToAddNewPromoCodeExceptionIfTextContainsANCharacter2() {
        String text = "TEST123@";
        LocalDate expirationDate = LocalDate.of(2024, 8, 18);
        Integer usagesAllowed = 10;
        Double discountAmount = 55.55;
        String discountCurrency = "USD";

        assertThatThrownBy(() -> promoCodeService.addNewFixedAmountPromoCode(
                text,
                expirationDate,
                usagesAllowed,
                discountAmount,
                discountCurrency
        )).isInstanceOf(FailedToAddNewPromoCodeException.class)
                .extracting(
                        "message",
                        "status",
                        "debugMessage"
                ).containsExactly(
                        "Failed to add new promo code.",
                        HttpStatus.BAD_REQUEST,
                        "Promo code text must only contain " +
                                "letters (a-z, A-Z) and digits (0-9)."
                );

        verify(promoCodeRepository, times(0))
                .save(any());
    }

    @Test
    public void shouldThrowFailedToAddNewPromoCodeExceptionIfTextContainsANCharacter3() {
        String text = "TEST123#";
        LocalDate expirationDate = LocalDate.of(2024, 8, 18);
        Integer usagesAllowed = 10;
        Double discountAmount = 55.55;
        String discountCurrency = "USD";

        assertThatThrownBy(() -> promoCodeService.addNewFixedAmountPromoCode(
                text,
                expirationDate,
                usagesAllowed,
                discountAmount,
                discountCurrency
        )).isInstanceOf(FailedToAddNewPromoCodeException.class)
                .extracting(
                        "message",
                        "status",
                        "debugMessage"
                ).containsExactly(
                        "Failed to add new promo code.",
                        HttpStatus.BAD_REQUEST,
                        "Promo code text must only contain " +
                                "letters (a-z, A-Z) and digits (0-9)."
                );

        verify(promoCodeRepository, times(0))
                .save(any());
    }

    @Test
    public void shouldThrowFailedToAddNewPromoCodeExceptionIfTextContainsANCharacter4() {
        String text = "TEST_123";
        LocalDate expirationDate = LocalDate.of(2024, 8, 18);
        Integer usagesAllowed = 10;
        Double discountAmount = 55.55;
        String discountCurrency = "USD";

        assertThatThrownBy(() -> promoCodeService.addNewFixedAmountPromoCode(
                text,
                expirationDate,
                usagesAllowed,
                discountAmount,
                discountCurrency
        )).isInstanceOf(FailedToAddNewPromoCodeException.class)
                .extracting(
                        "message",
                        "status",
                        "debugMessage"
                ).containsExactly(
                        "Failed to add new promo code.",
                        HttpStatus.BAD_REQUEST,
                        "Promo code text must only contain " +
                                "letters (a-z, A-Z) and digits (0-9)."
                );

        verify(promoCodeRepository, times(0))
                .save(any());
    }

    @Test
    public void shouldThrowFailedToAddNewPromoCodeExceptionIfTextContainsANCharacter5() {
        String text = "TEST-123";
        LocalDate expirationDate = LocalDate.of(2024, 8, 18);
        Integer usagesAllowed = 10;
        Double discountAmount = 55.55;
        String discountCurrency = "USD";

        assertThatThrownBy(() -> promoCodeService.addNewFixedAmountPromoCode(
                text,
                expirationDate,
                usagesAllowed,
                discountAmount,
                discountCurrency
        )).isInstanceOf(FailedToAddNewPromoCodeException.class)
                .extracting(
                        "message",
                        "status",
                        "debugMessage"
                ).containsExactly(
                        "Failed to add new promo code.",
                        HttpStatus.BAD_REQUEST,
                        "Promo code text must only contain " +
                                "letters (a-z, A-Z) and digits (0-9)."
                );

        verify(promoCodeRepository, times(0))
                .save(any());
    }

    @Test
    public void shouldThrowFailedToAddNewPromoCodeExceptionIfTextContainsANCharacter6() {
        String text = "TEST&123";
        LocalDate expirationDate = LocalDate.of(2024, 8, 18);
        Integer usagesAllowed = 10;
        Double discountAmount = 55.55;
        String discountCurrency = "USD";

        assertThatThrownBy(() -> promoCodeService.addNewFixedAmountPromoCode(
                text,
                expirationDate,
                usagesAllowed,
                discountAmount,
                discountCurrency
        )).isInstanceOf(FailedToAddNewPromoCodeException.class)
                .extracting(
                        "message",
                        "status",
                        "debugMessage"
                ).containsExactly(
                        "Failed to add new promo code.",
                        HttpStatus.BAD_REQUEST,
                        "Promo code text must only contain " +
                                "letters (a-z, A-Z) and digits (0-9)."
                );

        verify(promoCodeRepository, times(0))
                .save(any());
    }

    @Test
    public void shouldThrowFailedToAddNewPromoCodeExceptionIfTextContainsANCharacter7() {
        String text = "TEST(123)";
        LocalDate expirationDate = LocalDate.of(2024, 8, 18);
        Integer usagesAllowed = 10;
        Double discountAmount = 55.55;
        String discountCurrency = "USD";

        assertThatThrownBy(() -> promoCodeService.addNewFixedAmountPromoCode(
                text,
                expirationDate,
                usagesAllowed,
                discountAmount,
                discountCurrency
        )).isInstanceOf(FailedToAddNewPromoCodeException.class)
                .extracting(
                        "message",
                        "status",
                        "debugMessage"
                ).containsExactly(
                        "Failed to add new promo code.",
                        HttpStatus.BAD_REQUEST,
                        "Promo code text must only contain " +
                                "letters (a-z, A-Z) and digits (0-9)."
                );

        verify(promoCodeRepository, times(0))
                .save(any());
    }

    @Test
    public void shouldThrowFailedToAddNewPromoCodeExceptionIfPromoCodeAlreadyExists() {
        String text = "TEST123";
        LocalDate expirationDate = LocalDate.of(2024, 8, 18);
        Integer usagesAllowed = 10;
        Double discountAmount = 55.55;
        String discountCurrency = "USD";

        when(promoCodeRepository.existsByText(text)).thenReturn(true);

        assertThatThrownBy(() -> promoCodeService.addNewFixedAmountPromoCode(
                text,
                expirationDate,
                usagesAllowed,
                discountAmount,
                discountCurrency
        )).isInstanceOf(FailedToAddNewPromoCodeException.class)
                .extracting(
                        "message",
                        "status",
                        "debugMessage"
                ).containsExactly(
                        "Failed to add new promo code.",
                        HttpStatus.BAD_REQUEST,
                        String.format("Promo code text must be unique. "
                                + "'%s' promo code already exists.", text)
                );

        verify(promoCodeRepository, times(0))
               .save(any());
    }

    @Test
    public void shouldThrowFailedToAddNewPromoCodeExceptionIfExpirationDateIsNull() {
        String text = "TEST123";
        LocalDate expirationDate = null;
        Integer usagesAllowed = 10;
        Double discountAmount = 55.55;
        String discountCurrency = "USD";

        assertThatThrownBy(() -> promoCodeService.addNewFixedAmountPromoCode(
                text,
                expirationDate,
                usagesAllowed,
                discountAmount,
                discountCurrency
        )).isInstanceOf(FailedToAddNewPromoCodeException.class)
                .extracting(
                        "message",
                        "status",
                        "debugMessage"
                ).containsExactly(
                        "Failed to add new promo code.",
                        HttpStatus.BAD_REQUEST,
                        "Promo code expiration date can not be null."
                );

        verify(promoCodeRepository, times(0))
                .save(any());
    }

    @Test
    public void shouldThrowFailedToAddNewPromoCodeExceptionIfExpirationDateIsFromThePast() {
        String text = "TEST123";
        LocalDate expirationDate = LocalDate.now().minusDays(1);
        Integer usagesAllowed = 10;
        Double discountAmount = 55.55;
        String discountCurrency = "USD";

        assertThatThrownBy(() -> promoCodeService.addNewFixedAmountPromoCode(
                text,
                expirationDate,
                usagesAllowed,
                discountAmount,
                discountCurrency
        )).isInstanceOf(FailedToAddNewPromoCodeException.class)
                .extracting(
                        "message",
                        "status",
                        "debugMessage"
                ).containsExactly(
                        "Failed to add new promo code.",
                        HttpStatus.BAD_REQUEST,
                        "Promo code expiration date can not be in the past."
                );

        verify(promoCodeRepository, times(0))
                .save(any());
    }

    @Test
    public void shouldAddNewPromoCodeIfExpDateIsToday() {
        String text = "TEST123";
        LocalDate expirationDate = LocalDate.now();
        Integer usagesAllowed = 10;
        Double discountAmount = 55.55;
        String discountCurrency = "USD";

        when(promoCodeRepository.existsByText(text)).thenReturn(false);

        promoCodeService.addNewFixedAmountPromoCode(
                text,
                expirationDate,
                usagesAllowed,
                discountAmount,
                discountCurrency
        );

        ArgumentCaptor<PromoCode> captor =
                ArgumentCaptor.forClass(PromoCode.class);

        verify(promoCodeRepository, times(1))
                .save(captor.capture());

        PromoCode val = captor.getValue();

        assertThat(text).isEqualTo(val.getText());
        assertThat(expirationDate).isEqualTo(val.getExpirationDate());
        assertThat(usagesAllowed).isEqualTo(val.getUsagesTotal());
        assertThat(usagesAllowed).isEqualTo(val.getUsagesLeft());
        assertThat(discountAmount).isEqualTo(val.getDiscountAmount());
        assertThat(discountCurrency).isEqualTo(val.getDiscountCurrency());
        assertThat(val.getType()).isEqualTo(PromoCodeType.FIXED_AMOUNT);
        assertThat(val.getDiscountPercentage()).isNull();
    }

    @Test
    public void shouldAddNewPercentagePromoCode() {
        String text = "TEST123";
        LocalDate expirationDate = LocalDate.of(2024, 8, 18);
        Integer usagesAllowed = 10;
        Integer discountPercentage = 10;

        when(promoCodeRepository.existsByText(text)).thenReturn(false);

        promoCodeService.addNewPercentagePromoCode(
                text,
                expirationDate,
                usagesAllowed,
                discountPercentage
        );

        ArgumentCaptor<PromoCode> captor =
                ArgumentCaptor.forClass(PromoCode.class);

        verify(promoCodeRepository, times(1))
                .save(captor.capture());

        PromoCode val = captor.getValue();

        assertThat(text).isEqualTo(val.getText());
        assertThat(expirationDate).isEqualTo(val.getExpirationDate());
        assertThat(usagesAllowed).isEqualTo(val.getUsagesTotal());
        assertThat(usagesAllowed).isEqualTo(val.getUsagesLeft());
        assertThat(0.0).isEqualTo(val.getDiscountAmount());
        assertThat(val.getDiscountCurrency()).isNull();
        assertThat(PromoCodeType.PERCENTAGE).isEqualTo(val.getType());
        assertThat(10).isEqualTo(val.getDiscountPercentage());
    }

    @Test
    public void shouldThrowFailedToAddNewPromoCodeExceptionIfDiscountPercentageIsZero() {
        String text = "TEST123";
        LocalDate expirationDate = LocalDate.of(2024, 8, 18);
        Integer usagesAllowed = 10;
        Integer discountPercentage = 0;

        when(promoCodeRepository.existsByText(text)).thenReturn(false);

        assertThatThrownBy(() -> promoCodeService.addNewPercentagePromoCode(
                text,
                expirationDate,
                usagesAllowed,
                discountPercentage
        )).isInstanceOf(FailedToAddNewPromoCodeException.class)
                .extracting(
                        "message",
                        "status",
                        "debugMessage"
                ).containsExactly(
                        "Failed to add new promo code.",
                        HttpStatus.BAD_REQUEST,
                        "Promo code discount percentage can not be less than or equal zero."
                );

        verify(promoCodeRepository, times(0))
                .save(any());
    }

    @Test
    public void shouldThrowFailedToAddNewPromoCodeExceptionIfDiscountPercentageIsMoreThanHundred() {
        String text = "TEST123";
        LocalDate expirationDate = LocalDate.of(2024, 8, 18);
        Integer usagesAllowed = 10;
        Integer discountPercentage = 101;

        when(promoCodeRepository.existsByText(text)).thenReturn(false);

        assertThatThrownBy(() -> promoCodeService.addNewPercentagePromoCode(
                text,
                expirationDate,
                usagesAllowed,
                discountPercentage
        )).isInstanceOf(FailedToAddNewPromoCodeException.class)
                .extracting(
                        "message",
                        "status",
                        "debugMessage"
                ).containsExactly(
                        "Failed to add new promo code.",
                        HttpStatus.BAD_REQUEST,
                        "Promo code discount percentage can not be more than 100."
                );

        verify(promoCodeRepository, times(0))
                .save(any());
    }
}