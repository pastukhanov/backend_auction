package org.luxeloot.backend.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "lots")
public class Lot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "auction_id", nullable = false)
    private Auction auction;

    private String logo;
    private String status;

    private String name;

    private String title;
    private String description;
    private Integer timer;
    private double startingPrice;
    private double bet;
    private double currentPrice;
    private LocalDateTime endTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lot_owner_user_id")
    private User lotOwner;

    public boolean isActive() {
        return LocalDateTime.now().isBefore(endTime);
    }
}

