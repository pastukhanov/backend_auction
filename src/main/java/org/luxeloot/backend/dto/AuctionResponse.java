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
public class AuctionResponse {
    private Long id;
    private String title;
    private String logo; // Assuming this is a URL or a reference to the file location
    private String status;
    private String topic;
    private double startingPrice;
    private double currentPrice;
    private int lotsCount;
    private UserResponse user;
    private String authors;
    private int lotsTimer;
    private LocalDateTime auctionStartTime;
    private String description;
}
