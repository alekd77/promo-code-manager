package com.promocodes.promocodesmanager.purchase;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path="/purchases")
public class PurchaseController {
    private final PurchaseService purchaseService;

    @Autowired
    public PurchaseController(PurchaseService purchaseService) {
        this.purchaseService = purchaseService;
    }

    @PostMapping()
    public ResponseEntity<String> handlePurchase(
            @RequestParam(name = "productName")
            String productName,
            @RequestParam(name = "promoCodeText", required = false)
            String promoCodeText) {

        promoCodeText = promoCodeText != null
                ? promoCodeText
                : "";

        purchaseService.handlePurchase(
                productName,
                promoCodeText
        );

        String message = String.format(
                "'%s' has been successfully purchased.",
                productName
        );

        return new ResponseEntity<>(message, HttpStatus.CREATED);
    }
}
