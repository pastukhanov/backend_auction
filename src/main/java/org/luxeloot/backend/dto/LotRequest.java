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
public class LotRequest {

    private String name;

    private Long auctionId;

    private String title;
    private String description;
    private Integer timer;
    private double bet;

}
