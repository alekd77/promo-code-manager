package com.promocodes.promocodesmanager.purchase;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping(path="/purchases")
public class PurchaseController {
    private final PurchaseService purchaseService;

    @Autowired
    public PurchaseController(PurchaseService purchaseService) {
        this.purchaseService = purchaseService;
    }

    @PostMapping(params = {"productName", "promoCodeText"})
    public ResponseEntity<String> handlePurchase(
            @RequestParam String productName,
            @RequestParam Optional<String> promoCodeText) {

        purchaseService.handlePurchase(
                productName,
                promoCodeText.orElse("")
        );

        String message = String.format(
                "'%s' has been successfully purchased.",
                productName
        );

        return new ResponseEntity<>(message, HttpStatus.CREATED);
    }
}
