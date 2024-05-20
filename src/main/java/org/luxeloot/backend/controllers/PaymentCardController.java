package org.luxeloot.backend.controllers;


import org.luxeloot.backend.config.JwtService;
import org.luxeloot.backend.dto.PaymentCardRequest;
import org.luxeloot.backend.dto.PaymentCardResponse;
import org.luxeloot.backend.services.PaymentCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users/payment-card")
public class PaymentCardController {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private PaymentCardService paymentCardService;

    @PostMapping
    public ResponseEntity<PaymentCardResponse> createPaymentCard(@RequestBody PaymentCardRequest card, @RequestHeader("Authorization") String token) {
        String actualToken = token.split("\\s+")[1];
        String userName = jwtService.extractUsername(actualToken);
        PaymentCardResponse savedCard = paymentCardService.createPaymentCard(card, userName);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCard);
    }

    @GetMapping
    public ResponseEntity<PaymentCardResponse> getPaymentCard(@RequestHeader("Authorization") String token) {
        String actualToken = token.split("\\s+")[1];
        String userName = jwtService.extractUsername(actualToken);
        PaymentCardResponse userCard = paymentCardService.getPaymentCard(userName);
        return ResponseEntity.ok(userCard);
    }

    @PutMapping
    public ResponseEntity<PaymentCardResponse> updatePaymentCard(@RequestBody PaymentCardRequest card, @RequestHeader("Authorization") String token) {
        String actualToken = token.split("\\s+")[1];
        String userName = jwtService.extractUsername(actualToken);
        PaymentCardResponse savedCard = paymentCardService.updatePaymentCard(card, userName);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCard);
    }

    @PostMapping("/delete")
    public ResponseEntity<Void> deletePaymentCard(@RequestBody PaymentCardRequest card, @RequestHeader("Authorization") String token) {
        String actualToken = token.split("\\s+")[1];
        String userName = jwtService.extractUsername(actualToken);
        System.out.println("deleting");
        System.out.println(userName);
        paymentCardService.deletePaymentCard(card, userName);
        return ResponseEntity.noContent().build();
    }
}
