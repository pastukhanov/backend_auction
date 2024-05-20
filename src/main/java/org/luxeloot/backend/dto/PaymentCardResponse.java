package org.luxeloot.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentCardResponse {
    private String cardHolderName;
    private String cardNumber;
    private String cvv;
    private LocalDateTime expirationDate;
}
