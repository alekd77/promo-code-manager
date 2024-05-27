package com.promocodes.promocodesmanager.exception;

import com.promocodes.promocodesmanager.product.Product;
import com.promocodes.promocodesmanager.promocode.PromoCode;
import org.springframework.http.HttpStatus;

public class ProductDiscountCalculationException extends ApiException {
    private Product product;
    private PromoCode promoCode;

    public ProductDiscountCalculationException(HttpStatus httpStatus,
                                               String debugMessage,
                                               Product product,
                                               PromoCode promoCode) {
        super("Product discount calculation failed.",
                httpStatus,
                debugMessage
        );

        this.product = product;
        this.promoCode = promoCode;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public PromoCode getPromoCode() {
        return promoCode;
    }

    public void setPromoCode(PromoCode promoCode) {
        this.promoCode = promoCode;
    }
}
