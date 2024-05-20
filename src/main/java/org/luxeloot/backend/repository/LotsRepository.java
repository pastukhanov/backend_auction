package org.luxeloot.backend.repository;

import org.luxeloot.backend.models.Lot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LotsRepository extends JpaRepository<Lot, Long> {
    List<Lot> findByAuctionId(Long auctionId);
    List<Lot> findByLotOwnerId(Long userId);
}
