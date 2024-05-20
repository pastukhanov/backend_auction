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
public class LotResponse {

    private Long id;
    private String logo;
    private String status;

    private String name;
    private String title;

    private Long auctionId;

    private String owner;

    private String description;
    private Integer timer;
    private double startingPrice;
    private double bet;
    private double currentPrice;
    private LocalDateTime endTime;

}
