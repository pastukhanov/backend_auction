package org.luxeloot.backend.repository;

import org.luxeloot.backend.models.Auction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AuctionRepository extends JpaRepository<Auction, Long> {
    List <Auction> findAllByUserIdOrderByIdDesc(long userId);
}
