package com.promocodes.promocodesmanager.promocode;

import com.promocodes.promocodesmanager.exception.ExceptionMapper;
import com.promocodes.promocodesmanager.exception.PromoCodeNotFoundException;
import com.promocodes.promocodesmanager.product.Product;
import com.promocodes.promocodesmanager.product.ProductResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path="/promo-codes")
public class PromoCodeController {
    private final PromoCodeService promoCodeService;
    private final PromoCodeMapper promoCodeMapper;

    @Autowired
    public PromoCodeController(PromoCodeService promoCodeService,
                               PromoCodeMapper promoCodeMapper) {
        this.promoCodeService = promoCodeService;
        this.promoCodeMapper = promoCodeMapper;
    }

    @GetMapping
    public ResponseEntity<List<PromoCodeResponseDto>> getAllPromoCodes() {
        List<PromoCode> promoCodes = promoCodeService.getAllPromoCodes();
        List<PromoCodeResponseDto> promoCodeResponseDtoList = promoCodeMapper.toPromoCodesResponseDtoList(promoCodes);

        return new ResponseEntity<>(promoCodeResponseDtoList, HttpStatus.OK);
    }

    @GetMapping(params = "text")
    public ResponseEntity<PromoCodeResponseDto> getPromoCodeByText(@RequestParam String text) {
        PromoCode promoCode = promoCodeService.getPromoCodeByText(text);
        PromoCodeResponseDto promoCodeResponseDto = promoCodeMapper.toPromoCodeResponseDto(promoCode).orElseThrow();

        return new ResponseEntity<>(promoCodeResponseDto, HttpStatus.OK);
    }

    @PostMapping(path = "/fixed-amount")
    public ResponseEntity<String> addNewFixedAmountPromoCode(@RequestBody FixedAmountPromoCodeDto dto) {
        promoCodeService.addNewFixedAmountPromoCode(
                dto.getText(),
                dto.getExpirationDate(),
                dto.getUsagesAllowed(),
                dto.getDiscountAmount(),
                dto.getDiscountCurrency()
        );

        return new ResponseEntity<>("Promo code has been successfully added.", HttpStatus.CREATED);
    }
}
