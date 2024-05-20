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
public class AuctionRequest {
    private String title;
    private String logo; // Assuming this is a URL or a reference to the file location
    private String status;
    private String topic;
    private double startingPrice;
    private LocalDateTime auctionStartTime;
    private String authors;
    private int lotsTimer;
    private String description;
}
