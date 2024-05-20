package org.luxeloot.backend.repository;

import org.luxeloot.backend.models.PaymentCard;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentCardRepository extends JpaRepository<PaymentCard, Long> {
    Optional<PaymentCard> findByCardNumber(String cardNumber);
}
