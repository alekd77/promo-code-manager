package com.promocodes.promocodesmanager.promocode;

import com.promocodes.promocodesmanager.exception.FailedToAddNewPromoCodeException;
import com.promocodes.promocodesmanager.exception.PromoCodeNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Currency;
import java.util.List;

@Service
public class PromoCodeService {
    private final PromoCodeRepository promoCodeRepository;

    @Autowired
    public PromoCodeService(PromoCodeRepository promoCodeRepository) {
        this.promoCodeRepository = promoCodeRepository;
    }

    public List<PromoCode> getAllPromoCodes() {
        return promoCodeRepository.findAll();
    }

    public PromoCode getPromoCodeByText(String text) {
        return promoCodeRepository.findByText(text)
               .orElseThrow(PromoCodeNotFoundException::new);
    }

    @Transactional
    public PromoCode addNewFixedAmountPromoCode(String text,
                                                LocalDate expirationDate,
                                                Integer usagesAllowed,
                                                Double discountAmount,
                                                String discountCurrency) {
        validateCorePromoCodeData(text, expirationDate, usagesAllowed);

        if (discountAmount == null) {
            throw new FailedToAddNewPromoCodeException(
                    HttpStatus.BAD_REQUEST,
                    "Promo code discount amount can not be null."
            );
        }

        if (discountAmount <= 0.0) {
            throw new FailedToAddNewPromoCodeException(
                    HttpStatus.BAD_REQUEST,
                    "Promo code discount amount can not be less than or equal zero."
            );
        }

        if (discountCurrency == null || discountCurrency.isEmpty()) {
            throw new FailedToAddNewPromoCodeException(
                    HttpStatus.BAD_REQUEST,
                    "Promo code discount currency can not be null or empty."
            );
        }

        if (!isValidCurrency(discountCurrency)) {
            throw new FailedToAddNewPromoCodeException(
                    HttpStatus.BAD_REQUEST,
                    String.format("Discount currency '%s' is invalid.",
                            discountCurrency)
            );
        }

        try {
            Double formatedDiscountAmount =
                    BigDecimal.valueOf(discountAmount)
                            .setScale(2, RoundingMode.DOWN)
                            .doubleValue();
            String formattedDiscountCurrency =
                    discountCurrency.toUpperCase();

            PromoCode promoCode = new PromoCode();

            promoCode.setText(text);
            promoCode.setExpirationDate(expirationDate);
            promoCode.setUsagesTotal(usagesAllowed);
            promoCode.setUsagesLeft(usagesAllowed);
            promoCode.setType(PromoCodeType.FIXED_AMOUNT);
            promoCode.setDiscountAmount(formatedDiscountAmount);
            promoCode.setDiscountCurrency(formattedDiscountCurrency);
            promoCode.setDiscountPercentage(null);

            return promoCodeRepository.save(promoCode);
        } catch (Exception ex) {
            throw new FailedToAddNewPromoCodeException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Unknown error occurred."
            );
        }
    }

    @Transactional
    public PromoCode addNewPercentagePromoCode(String text,
                                               LocalDate expirationDate,
                                               Integer usagesAllowed,
                                               Integer discountPercentage) {
        validateCorePromoCodeData(text, expirationDate, usagesAllowed);

        if (discountPercentage == null) {
            throw new FailedToAddNewPromoCodeException(
                    HttpStatus.BAD_REQUEST,
                    "Promo code discount percentage can not be null."
            );
        }

        if (discountPercentage <= 0) {
            throw new FailedToAddNewPromoCodeException(
                    HttpStatus.BAD_REQUEST,
                    "Promo code discount percentage can not be less than or equal zero."
            );
        }

        if (discountPercentage > 100) {
            throw new FailedToAddNewPromoCodeException(
                    HttpStatus.BAD_REQUEST,
                    "Promo code discount percentage can not be more than 100."
            );
        }

        try {
            PromoCode promoCode = new PromoCode();

            promoCode.setText(text);
            promoCode.setExpirationDate(expirationDate);
            promoCode.setUsagesTotal(usagesAllowed);
            promoCode.setUsagesLeft(usagesAllowed);
            promoCode.setType(PromoCodeType.PERCENTAGE);
            promoCode.setDiscountAmount(0.0);
            promoCode.setDiscountCurrency(null);
            promoCode.setDiscountPercentage(discountPercentage);

            return promoCodeRepository.save(promoCode);
        } catch (Exception ex) {
            throw new FailedToAddNewPromoCodeException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Unknown error occurred"
            );
        }
    }

    @Transactional
    public void decrementPromoCodeUsagesLeft(String promoCodeText) {
        PromoCode promoCode = getPromoCodeByText(promoCodeText);
        promoCode.setUsagesLeft(promoCode.getUsagesLeft() - 1);
        promoCodeRepository.save(promoCode);
    }

    private void validateCorePromoCodeData(String text, LocalDate expirationDate, Integer usagesAllowed) {
        if (text == null || text.isEmpty()) {
            throw new FailedToAddNewPromoCodeException(
                    HttpStatus.BAD_REQUEST,
                    "Promo code text can not be null or empty."
            );
        }

        if (text.length() < 3 || text.length() > 24) {
            throw new FailedToAddNewPromoCodeException(
                    HttpStatus.BAD_REQUEST,
                    "Promo code text must be " +
                            "between 3 and 24 characters long."
            );
        }

        if (!text.chars().allMatch(Character::isLetterOrDigit)) {
            throw new FailedToAddNewPromoCodeException(
                    HttpStatus.BAD_REQUEST,
                    "Promo code text must only contain " +
                            "letters (a-z, A-Z) and digits (0-9)."
            );
        }

        if (promoCodeRepository.existsByText(text)) {
            throw new FailedToAddNewPromoCodeException(
                    HttpStatus.BAD_REQUEST,
                    String.format("Promo code text must be unique. "
                            + "'%s' promo code already exists.", text)
            );
        }

        if (expirationDate == null) {
            throw new FailedToAddNewPromoCodeException(
                    HttpStatus.BAD_REQUEST,
                    "Promo code expiration date can not be null."
            );
        }

        if (expirationDate.isBefore(LocalDate.now())) {
            throw new FailedToAddNewPromoCodeException(
                    HttpStatus.BAD_REQUEST,
                    "Promo code expiration date can not be in the past."
            );
        }

        if (usagesAllowed == null) {
            throw new FailedToAddNewPromoCodeException(
                    HttpStatus.BAD_REQUEST,
                    "Promo code usages allowed can not be null."
            );
        }

        if (usagesAllowed < 1) {
            throw new FailedToAddNewPromoCodeException(
                    HttpStatus.BAD_REQUEST,
                    "Promo code usages allowed can not be less than one."
            );
        }
    }

    private boolean isValidCurrency(String currency) {
        try {
            String formattedCurrency = currency.toUpperCase();

            return Currency.getAvailableCurrencies()
                    .contains(Currency.getInstance(formattedCurrency));
        } catch (Exception ex) {
            return false;
        }
    }
}
