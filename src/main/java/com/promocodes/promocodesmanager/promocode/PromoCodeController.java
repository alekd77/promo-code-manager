package com.promocodes.promocodesmanager.promocode;

import com.promocodes.promocodesmanager.exception.ExceptionMapper;
import com.promocodes.promocodesmanager.product.Product;
import com.promocodes.promocodesmanager.product.ProductResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
