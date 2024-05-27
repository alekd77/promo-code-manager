package com.promocodes.promocodesmanager.promocode;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PromoCodeMapper {
    public FixedAmountPromoCodeResponseDto toFixedAmountPromoCodeResponseDto(PromoCode promoCode) {
        FixedAmountPromoCodeResponseDto dto = new FixedAmountPromoCodeResponseDto();

        dto.setId(
                promoCode != null && promoCode.getPromoCodeId() != null
                        ? promoCode.getPromoCodeId()
                        : -1L
        );

        dto.setText(
                promoCode != null && promoCode.getText() != null
                        ? promoCode.getText()
                        : ""
        );

        dto.setExpirationDate(
                promoCode != null && promoCode.getExpirationDate() != null
                        ? promoCode.getExpirationDate()
                        : null
        );

        dto.setUsagesTotal(
                promoCode != null && promoCode.getUsagesTotal() != null
                        ? promoCode.getUsagesTotal()
                        : 0
        );

        dto.setUsagesLeft(
                promoCode != null && promoCode.getUsagesLeft() != null
                        ? promoCode.getUsagesLeft()
                        : 0
        );

        dto.setType(
                promoCode != null && promoCode.getType() != null
                        ? promoCode.getType()
                        : null
        );

        dto.setDiscountAmount(
                promoCode != null && promoCode.getDiscountAmount() != null
                        ? promoCode.getDiscountAmount()
                        : 0.0
        );

        dto.setDiscountCurrency(
                promoCode != null && promoCode.getDiscountCurrency() != null
                        ? promoCode.getDiscountCurrency()
                        : ""
        );

        return dto;
    }

    public PercentagePromoCodeResponseDto toPercentagePromoCodeResponseDto(PromoCode promoCode) {
        PercentagePromoCodeResponseDto dto = new PercentagePromoCodeResponseDto();

        dto.setId(
                promoCode != null && promoCode.getPromoCodeId() != null
                        ? promoCode.getPromoCodeId()
                        : -1L
        );

        dto.setText(
                promoCode != null && promoCode.getText() != null
                        ? promoCode.getText()
                        : ""
        );

        dto.setExpirationDate(
                promoCode != null && promoCode.getExpirationDate() != null
                        ? promoCode.getExpirationDate()
                        : null
        );

        dto.setUsagesTotal(
                promoCode != null && promoCode.getUsagesTotal() != null
                        ? promoCode.getUsagesTotal()
                        : 0
        );

        dto.setUsagesLeft(
                promoCode != null && promoCode.getUsagesLeft() != null
                        ? promoCode.getUsagesLeft()
                        : 0
        );

        dto.setType(
                promoCode != null && promoCode.getType() != null
                        ? promoCode.getType()
                        : null
        );

        dto.setDiscountPercentage(
                promoCode != null && promoCode.getDiscountPercentage() != null
                        ? promoCode.getDiscountPercentage()
                        : 0
        );

        return dto;
    }

    public Optional<PromoCodeResponseDto> toPromoCodeResponseDto(PromoCode promoCode) {
        if (promoCode == null || promoCode.getType() == null) {
            return Optional.empty();
        }

        if (promoCode.getType() == PromoCodeType.FIXED_AMOUNT) {
            return Optional.of(toFixedAmountPromoCodeResponseDto(promoCode));
        }

        if (promoCode.getType() == PromoCodeType.PERCENTAGE) {
            return Optional.of(toPercentagePromoCodeResponseDto(promoCode));
        }

        return Optional.empty();
    }

    public List<PromoCodeResponseDto> toPromoCodesResponseDtoList(List<PromoCode> promoCodes) {
        List<PromoCodeResponseDto> result = new ArrayList<>();

        for (PromoCode promoCode : promoCodes) {
            toPromoCodeResponseDto(promoCode).ifPresent(result::add);
        }

        return result;
    }
}
