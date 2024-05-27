package com.promocodes.promocodesmanager.promocode;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Service
class PromoCodeMapperTest {
    private PromoCodeMapper promoCodeMapper;

    @BeforeEach
    void setUp() {
        promoCodeMapper = new PromoCodeMapper();
    }

    @Test
    public void shouldReturnFixedAmountPromoCodeResponseDto() {
        PromoCode promoCode = new PromoCode(
                123L,
                "SUMMER2024",
                LocalDate.of(2024, 8, 1),
                5,
                3,
                null,
                55.55,
                "USD",
                null
        );

        FixedAmountPromoCodeResponseDto dto =
                promoCodeMapper.toFixedAmountPromoCodeResponseDto(promoCode);

        assertThat(123L).
                isEqualTo(promoCode.getPromoCodeId());
        assertThat("SUMMER2024").
                isEqualTo(promoCode.getText());
        assertThat(LocalDate.of(2024, 8, 1))
                .isEqualTo(promoCode.getExpirationDate());
        assertThat(5)
                .isEqualTo(promoCode.getUsagesTotal());
        assertThat(3)
                .isEqualTo(promoCode.getUsagesLeft());
        assertThat(PromoCodeType.FIXED_AMOUNT)
                .isEqualTo(promoCode.getType());
        assertThat(55.55)
                .isEqualTo(promoCode.getDiscountAmount());
        assertThat("USD")
                .isEqualTo(promoCode.getDiscountCurrency());
    }

    @Test
    public void shouldReturnEmptyPromoCodeResponseDto() {
        PromoCode promoCode = null;

        FixedAmountPromoCodeResponseDto dto =
                promoCodeMapper.toFixedAmountPromoCodeResponseDto(promoCode);

        assertThat(-1L)
               .isEqualTo(dto.getPromoCodeId());
        assertThat("")
               .isEqualTo(dto.getText());
        assertThat(dto.getExpirationDate()).isNull();
        assertThat(0)
               .isEqualTo(dto.getUsagesTotal());
        assertThat(0)
               .isEqualTo(dto.getUsagesLeft());
        assertThat(dto.getType()).isNull();
        assertThat(0.0)
               .isEqualTo(dto.getDiscountAmount());
        assertThat("")
               .isEqualTo(dto.getDiscountCurrency());
    }

    @Test
    public void shouldReturnPercentagePromoCodeResponseDto() {
        PromoCode percentagePromoCode = new PromoCode(
                244L,
                "LOL23",
                LocalDate.of(2024, 8, 1),
                1,
                1,
                PromoCodeType.PERCENTAGE,
                null,
                null,
                0.9
        );

        PercentagePromoCodeResponseDto dto =
                promoCodeMapper.toPercentagePromoCodeResponseDto(percentagePromoCode);

        assertThat(244L)
                .isEqualTo(percentagePromoCode.getPromoCodeId());
        assertThat("LOL23")
                .isEqualTo(percentagePromoCode.getText());
        assertThat(LocalDate.of(2024, 8, 1))
                .isEqualTo(percentagePromoCode.getExpirationDate());
        assertThat(1)
                .isEqualTo(percentagePromoCode.getUsagesTotal());
        assertThat(1)
                .isEqualTo(percentagePromoCode.getUsagesLeft());
        assertThat(PromoCodeType.PERCENTAGE)
                .isEqualTo(percentagePromoCode.getType());
        assertThat(0.9)
                .isEqualTo(percentagePromoCode.getDiscountPercentage());
    }

    @Test
    public void shouldReturnPromoCodeResponseDtoList() {
        PromoCode fixedPromoCode = new PromoCode(
                123L,
                "SUMMER2024",
                LocalDate.of(2024, 8, 1),
                5,
                3,
                PromoCodeType.FIXED_AMOUNT,
                55.55,
                "USD",
                null
        );

        PromoCode percentagePromoCode = new PromoCode(
                244L,
                "LOL23",
                LocalDate.of(2024, 8, 1),
                1,
                1,
                PromoCodeType.PERCENTAGE,
                null,
                null,
                0.9
        );

        List<PromoCode> promoCodes = new ArrayList<>();
        promoCodes.add(fixedPromoCode);
        promoCodes.add(percentagePromoCode);

        List<PromoCodeResponseDto> dtos =
                promoCodeMapper.toPromoCodesResponseDtoList(promoCodes);

        assertThat(2).isEqualTo(dtos.size());

        FixedAmountPromoCodeResponseDto fixedAmountPromoCodeDto =
                (FixedAmountPromoCodeResponseDto) dtos.get(0);
        assertThat(123L)
                .isEqualTo(fixedAmountPromoCodeDto.getPromoCodeId());
        assertThat("SUMMER2024")
                .isEqualTo(fixedAmountPromoCodeDto.getText());
        assertThat(LocalDate.of(2024, 8, 1))
                .isEqualTo(fixedAmountPromoCodeDto.getExpirationDate());
        assertThat(5)
                .isEqualTo(fixedAmountPromoCodeDto.getUsagesTotal());
        assertThat(3)
                .isEqualTo(fixedAmountPromoCodeDto.getUsagesLeft());
        assertThat(PromoCodeType.FIXED_AMOUNT).isEqualTo(fixedAmountPromoCodeDto.getType());
        assertThat(55.55)
                .isEqualTo(fixedAmountPromoCodeDto.getDiscountAmount());
        assertThat("USD")
                .isEqualTo(fixedAmountPromoCodeDto.getDiscountCurrency());

        PercentagePromoCodeResponseDto percentagePromoCodeDto =
                (PercentagePromoCodeResponseDto) dtos.get(1);
        assertThat(244L)
                .isEqualTo(percentagePromoCodeDto.getPromoCodeId());
        assertThat("LOL23")
                .isEqualTo(percentagePromoCodeDto.getText());
        assertThat(LocalDate.of(2024, 8, 1))
                .isEqualTo(percentagePromoCodeDto.getExpirationDate());
        assertThat(1)
                .isEqualTo(percentagePromoCodeDto.getUsagesTotal());
        assertThat(1)
                .isEqualTo(percentagePromoCodeDto.getUsagesLeft());
        assertThat(PromoCodeType.PERCENTAGE)
                .isEqualTo(percentagePromoCodeDto.getType());
        assertThat(0.9)
                .isEqualTo(percentagePromoCodeDto.getDiscountPercentage());
    }
}