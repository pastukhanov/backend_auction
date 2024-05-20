package org.luxeloot.backend.services;


import org.luxeloot.backend.dto.PaymentCardRequest;
import org.luxeloot.backend.dto.PaymentCardResponse;
import org.luxeloot.backend.models.PaymentCard;
import org.luxeloot.backend.repository.PaymentCardRepository;
import org.luxeloot.backend.models.User;
import org.luxeloot.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class PaymentCardService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PaymentCardRepository paymentCardRepository;

    public PaymentCardResponse createPaymentCard(PaymentCardRequest card, String userName) {
        User user = userRepository.findByEmail(userName)
                .orElseThrow(() -> new RuntimeException("User not found"));
        System.out.println(user);
        PaymentCard paymentCard = PaymentCard.builder()
                .cardNumber(card.getCardNumber())
                .cardHolderName(card.getCardHolderName())
                .expirationDate(card.getExpirationDate())
                .cvv(card.getCvv())
                .user(user)
                .build();

        user.setPaymentCard(paymentCard);

        System.out.println(paymentCardRepository.save(paymentCard));
        System.out.println(userRepository.save(user));

        return convertPaymentCardToResponse(paymentCard);
    }

    private PaymentCardResponse convertPaymentCardToResponse(PaymentCard paymentCard) {
        PaymentCardResponse cardResponse = PaymentCardResponse.builder()
                .cardNumber(paymentCard.getCardNumber())
                .cardHolderName(paymentCard.getCardHolderName())
                .expirationDate(paymentCard.getExpirationDate())
                .cvv(paymentCard.getCvv())
                .build();
        return cardResponse;
    }

    public PaymentCardResponse getPaymentCard(String userName) {
        User user = userRepository.findByEmail(userName)
                .orElseThrow(() -> new RuntimeException("User not found"));
        PaymentCard paymentCard = user.getPaymentCard();
        System.out.println(paymentCard);
        if (paymentCard == null) {
            return PaymentCardResponse.builder().build();
        }
        return convertPaymentCardToResponse(paymentCard);
    }

    public PaymentCardResponse updatePaymentCard(PaymentCardRequest card, String userName) {
        User user = userRepository.findByEmail(userName)
                .orElseThrow(() -> new RuntimeException("User not found"));
        PaymentCard paymentCard = user.getPaymentCard();
        if (paymentCard == null) {
            paymentCard = PaymentCard.builder().build();
        }
        paymentCard.setCardHolderName(card.getCardHolderName());
        paymentCard.setCardNumber(card.getCardNumber());
        paymentCard.setExpirationDate(card.getExpirationDate());
        paymentCard.setUser(user);
        paymentCard.setCvv(card.getCvv());

        user.setPaymentCard(paymentCard);

        PaymentCard updatedPaymentCard = paymentCardRepository.save(paymentCard);
        userRepository.save(user);

        return convertPaymentCardToResponse(updatedPaymentCard);
    }

    public void deletePaymentCard(PaymentCardRequest card, String userName) {
        User user = userRepository.findByEmail(userName)
                .orElseThrow(() -> new RuntimeException("User not found"));
        PaymentCard paymentCard = user.getPaymentCard();
        if (paymentCard != null) {
            System.out.println("deleting " + paymentCard);
            user.setPaymentCard(null);
            paymentCardRepository.delete(paymentCard);
        }
    }
}
