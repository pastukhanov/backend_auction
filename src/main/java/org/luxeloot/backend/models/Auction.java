package org.luxeloot.backend.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "auctions")
public class Auction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String logo; // Assuming this is a URL or a reference to the file location
    private String status;
    private String topic;
    private double startingPrice;
    private int lotsCount;

    private LocalDateTime auctionStartTime; // Consider using java.time.LocalDate for date-only data

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id") // This column will hold the foreign key to the user table
    private User user;

    private String authors;
    private int lotsTimer;

    @Column(length = 2048) // Increase column length for longer text
    private String description;

    @ManyToMany(mappedBy = "favoriteAuctions", fetch = FetchType.LAZY)
    private List<User> favoritedByUsers;

    @OneToMany(mappedBy = "auction", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Lot> lots;

    public int getLotsCount() {
        if (lots == null) {
            return 0;
        }
        return lots.size();
    }

    public String getStatus() {
        LocalDateTime now = LocalDateTime.now();
        if (auctionStartTime.isAfter(now)) {
            return "Планируется";
        } else if (!auctionStartTime.isAfter(now) && !auctionStartTime.plusHours(1).isBefore(now)) {
            return "Проводится";
        } else if (auctionStartTime.plusHours(1).isBefore(now)) {
            return "Завершился";
        } else {
            return "Не определен";
        }
    }

    public void endAuctionNow() {
        auctionStartTime = LocalDateTime.of(2000, 1, 1, 0, 0);
    }

    public double getCurrentPrice() {
        if (lots == null || lots.isEmpty()) {
            return startingPrice;
        }
        return lots.stream().map(lot -> lot.getBet()).reduce(0.0, Double::max);
    }

    @Override
    public String toString() {
        return Auction.class.getSimpleName() + " [id=" + id + ", title=" + title + ", logo=" + logo + " ]";
    }
}