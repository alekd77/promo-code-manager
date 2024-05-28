package com.promocodes.promocodesmanager.product;

import com.promocodes.promocodesmanager.exception.FailedToAddNewProductException;
import com.promocodes.promocodesmanager.exception.FailedToUpdateProductException;
import com.promocodes.promocodesmanager.exception.ProductDiscountCalculationException;
import com.promocodes.promocodesmanager.exception.ProductNotFoundException;
import com.promocodes.promocodesmanager.promocode.PromoCode;
import com.promocodes.promocodesmanager.promocode.PromoCodeService;
import com.promocodes.promocodesmanager.promocode.PromoCodeType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Currency;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final PromoCodeService promoCodeService;

    @Autowired
    public ProductService(ProductRepository productRepository,
                          PromoCodeService promoCodeService) {
        this.productRepository = productRepository;
        this.promoCodeService = promoCodeService;
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product findProductByName(String name) {
        if (name == null || name.isEmpty()) {
            throw new ProductNotFoundException(
                    "Product name is null or empty."
            );
        }

        String formattedName = StringUtils.capitalize(name);
        Optional<Product> optionalProduct =
                productRepository.findByName(formattedName);

        if (optionalProduct.isEmpty()) {
            throw new ProductNotFoundException();
        }

        return optionalProduct.get();
    }

    @Transactional
    public Product addNewProduct(String name,
                                 String description,
                                 Double price,
                                 String currency) {
        if (name == null || name.isEmpty()) {
            throw new FailedToAddNewProductException(
                    HttpStatus.BAD_REQUEST,
                    "Name of new product can not be null or empty"
            );
        }

        if (productRepository.existsByName(StringUtils.capitalize(name))) {
            throw new FailedToAddNewProductException(
                    HttpStatus.BAD_REQUEST,
                    String.format("'%s' product name is taken", name)
            );
        }

        if (price == null) {
            throw new FailedToAddNewProductException(
                    HttpStatus.BAD_REQUEST,
                    "Price of new product can not be null or empty"
            );
        }

        if (price < 0) {
            throw new FailedToAddNewProductException(
                    HttpStatus.BAD_REQUEST,
                    "Price of new product can not be less than zero"
            );
        }

        if (currency == null || currency.isEmpty()) {
            throw new FailedToAddNewProductException(
                    HttpStatus.BAD_REQUEST,
                    "Currency of new product can not be null or empty"
            );
        }

        if (!isValidCurrency(currency)) {
            throw new FailedToAddNewProductException(
                    HttpStatus.BAD_REQUEST,
                    String.format("Currency '%s' is invalid", currency)
            );
        }

        try {
            String formatedName = StringUtils.capitalize(name);
            String formatedDescription = StringUtils.capitalize(description);
            Double formatedPrice = BigDecimal.valueOf(price)
                    .setScale(2, RoundingMode.DOWN)
                    .doubleValue();
            String formattedCurrency = currency.toUpperCase();

            Product product = new Product();
            product.setName(formatedName);
            product.setDescription(formatedDescription);
            product.setPrice(formatedPrice);
            product.setCurrency(formattedCurrency);

            return productRepository.save(product);
        } catch (Exception ex) {
            throw new FailedToAddNewProductException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Unknown error occurred"
            );
        }
    }

    @Transactional
    public Product updateProduct(String name,
                                 String newName,
                                 String newDescription,
                                 Double newPrice,
                                 String newCurrency) {

        Product product = findProductByName(name);

        if (newName != null && !newName.isEmpty()) {
            String formattedNewName = StringUtils.capitalize(newName);

            if (!Objects.equals(product.getName(), formattedNewName)
                    && productRepository.existsByName(formattedNewName)) {
                throw new FailedToUpdateProductException(
                        HttpStatus.BAD_REQUEST,
                        String.format("Product '%s' already exists.",
                                formattedNewName
                        )
                );
            }

            product.setName(formattedNewName);
        }

        if (newDescription != null) {
            String formattedNewDescription =
                    StringUtils.capitalize(newDescription);
            product.setDescription(formattedNewDescription);
        }

        if (newPrice != null) {
            if (newPrice < 0) {
                throw new FailedToUpdateProductException(
                        HttpStatus.BAD_REQUEST,
                        "Price of the product can not be less than zero."
                );
            }

            Double formatedNewPrice = BigDecimal.valueOf(newPrice)
                    .setScale(2, RoundingMode.DOWN)
                    .doubleValue();
            product.setPrice(formatedNewPrice);
        }

        if (newCurrency != null) {
            if (!isValidCurrency(newCurrency)) {
                throw new FailedToUpdateProductException(
                        HttpStatus.BAD_REQUEST,
                        String.format("Currency '%s' is not valid.", newCurrency)
                );
            }

            String formattedNewCurrency = newCurrency.toUpperCase();
            product.setCurrency(formattedNewCurrency);
        }

        try {
            return productRepository.save(product);
        } catch (Exception ex) {
            throw new FailedToUpdateProductException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Unknown error occurred"
            );
        }
    }

    public Double calculateDiscountProductPrice(String productName,
                                                String promoCodeText) {
        Product product = findProductByName(productName);
        PromoCode promoCode = promoCodeService
                .getPromoCodeByText(promoCodeText);

        if (product.getPrice() == null || product.getPrice() < 0.0) {
            throw new ProductDiscountCalculationException(
                    HttpStatus.BAD_REQUEST,
                    "Product data are invalid.",
                    product,
                    promoCode
            );
        }

        if (promoCode.getExpirationDate() == null
                || promoCode.getUsagesLeft() == null
                || promoCode.getType() == null) {
            throw new ProductDiscountCalculationException(
                    HttpStatus.BAD_REQUEST,
                    "Promo code data are invalid.",
                    product,
                    promoCode
            );
        }

        if (promoCode.getExpirationDate().isBefore(LocalDate.now())) {
            throw new ProductDiscountCalculationException(
                    HttpStatus.BAD_REQUEST,
                    "Promo code is expired.",
                    product,
                    promoCode
            );
        }

        if (promoCode.getUsagesLeft() < 1) {
            throw new ProductDiscountCalculationException(
                    HttpStatus.BAD_REQUEST,
                    "Promo code is used up.",
                    product,
                    promoCode
            );
        }

        if (promoCode.getType() == PromoCodeType.FIXED_AMOUNT) {
            return calculateFixedAmountDiscountPrice(
                    product,
                    promoCode
            );
        }

        if (promoCode.getType() == PromoCodeType.PERCENTAGE) {
            return calculatePercentageDiscountPrice(
                    product,
                    promoCode
            );
        }

        throw new ProductDiscountCalculationException(
                HttpStatus.BAD_REQUEST,
                "Unsupported promo code type.",
                product,
                promoCode
        );
    }

    private Double calculateFixedAmountDiscountPrice(Product product,
                                                     PromoCode promoCode) {

        if (promoCode.getDiscountAmount() == null
                || promoCode.getDiscountAmount() < 0.0
                || promoCode.getDiscountCurrency() == null) {
            throw new ProductDiscountCalculationException(
                    HttpStatus.BAD_REQUEST,
                    "Promo code is invalid.",
                    product,
                    promoCode
            );
        }

        if (!promoCode.getDiscountCurrency().equals(product.getCurrency())) {
            throw new ProductDiscountCalculationException(
                    HttpStatus.BAD_REQUEST,
                    String.format(
                            "Currencies of product '%s' and promo code '%s' do not match.",
                            product.getName(),
                            promoCode.getText()
                    ),
                    product,
                    promoCode
            );
        }

        double discountPrice = product.getPrice() -
                promoCode.getDiscountAmount();

        return discountPrice > 0
                ? discountPrice
                : 0;
    }

    private Double calculatePercentageDiscountPrice(Product product,
                                                    PromoCode promoCode) {

        if (promoCode.getDiscountPercentage() == null
                || promoCode.getDiscountPercentage() <= 0
                || promoCode.getDiscountPercentage() > 100) {
            throw new ProductDiscountCalculationException(
                    HttpStatus.BAD_REQUEST,
                    "Promo code is invalid.",
                    product,
                    promoCode
            );
        }

        Double discountFactor = Double.valueOf(
                promoCode.getDiscountPercentage()) / 100.0;
        Double discountAmount = product.getPrice() * discountFactor;

        return product.getPrice() - discountAmount;
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
