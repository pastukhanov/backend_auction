package org.luxeloot.backend.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "payment_card")
public class PaymentCard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String cardHolderName;
    private String cardNumber;
    private String cvv;
    private LocalDateTime expirationDate;

    @OneToOne(mappedBy = "paymentCard")
    @ToString.Exclude // Avoid recursive call in toString
    @EqualsAndHashCode.Exclude // Avoid recursive call in equals and hashCode
    private User user;

    @Override
    public String toString() {
        return "PaymentCard{" +
                "id=" + id +
                ", cardNumber='" + cardNumber + '\'' +
                '}';
    }

}
