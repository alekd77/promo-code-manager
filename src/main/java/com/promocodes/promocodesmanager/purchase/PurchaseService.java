package com.promocodes.promocodesmanager.purchase;

import com.promocodes.promocodesmanager.exception.ProductDiscountCalculationException;
import com.promocodes.promocodesmanager.exception.PromoCodeNotFoundException;
import com.promocodes.promocodesmanager.exception.PurchaseException;
import com.promocodes.promocodesmanager.product.Product;
import com.promocodes.promocodesmanager.product.ProductService;
import com.promocodes.promocodesmanager.promocode.PromoCode;
import com.promocodes.promocodesmanager.promocode.PromoCodeService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PurchaseService {
    private final PurchaseRepository purchaseRepository;
    private final ProductService productService;
    private final PromoCodeService promoCodeService;

    @Autowired
    public PurchaseService(PurchaseRepository purchaseRepository,
                           ProductService productService,
                           PromoCodeService promoCodeService) {
        this.purchaseRepository = purchaseRepository;
        this.productService = productService;
        this.promoCodeService = promoCodeService;
    }

    public List<List<Purchase>> findPurchasesGroupedByCurrency() {
        try {
            return purchaseRepository.findAll()
                    .stream()
                    .collect(Collectors.groupingBy(Purchase::getCurrency))
                    .values()
                    .stream()
                    .toList();
        } catch (Exception ex) {
            return Collections.emptyList();
        }
    }

    @Transactional
    public Purchase handlePurchase(String productName,
                                   String promoCodeText) {
        Product product = productService.findProductByName(productName);
        Double discountAmount = 0.0;

        try {
            if (promoCodeText != null && !promoCodeText.isEmpty()) {
                discountAmount = productService
                        .calculateDiscountProductPrice(
                                productName,
                                promoCodeText
                        );

                promoCodeService.decrementPromoCodeUsagesLeft(
                        promoCodeText
                );
            }

            Purchase purchase = new Purchase();

            purchase.setProduct(product);
            purchase.setPurchaseDate(LocalDate.now());
            purchase.setRegularPrice(product.getPrice());
            purchase.setDiscountAmount(discountAmount);
            purchase.setCurrency(product.getCurrency());

            return purchaseRepository.save(purchase);

        } catch (PromoCodeNotFoundException ex) {
            throw new PurchaseException(
                    HttpStatus.BAD_REQUEST,
                    "Promo code does not exist. " +
                            "Please enter a valid promo code or leave it empty."
            );

        } catch (ProductDiscountCalculationException ex) {
            throw new PurchaseException(
                    HttpStatus.BAD_REQUEST,
                    String.format(
                            "Failed to use promo code due to: %s " +
                            "Please enter a valid promo code or leave it empty.",
                            ex.getDebugMessage()
                    )
            );
        } catch (Exception ex) {
            throw new PurchaseException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Unknown error occurred"
            );
        }
    }
}
