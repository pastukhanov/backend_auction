package org.luxeloot.backend.services;


import org.luxeloot.backend.dto.LotRequest;
import org.luxeloot.backend.dto.LotResponse;
import org.luxeloot.backend.models.Auction;
import org.luxeloot.backend.models.Lot;
import org.luxeloot.backend.models.Role;
import org.luxeloot.backend.models.User;
import org.luxeloot.backend.repository.AuctionRepository;
import org.luxeloot.backend.repository.LotsRepository;
import org.luxeloot.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;


import java.util.List;
import java.util.stream.Collectors;

@Service
public class LotsService {

    @Autowired
    private LotsRepository lotsRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuctionRepository auctionRepository;


    public List<LotResponse> getAllLots(String username) {
        User user = userRepository.findByEmail(username).orElseThrow(() -> new RuntimeException("User not found"));
        List<Lot> lots = lotsRepository.findByLotOwnerId(user.getId());
        return getProcessedLotResponses(lots);
    }

    private List<LotResponse> getProcessedLotResponses(List<Lot> lots) {
        List<LotResponse> lotResponses = lots
                .stream()
                .map(lot -> {
                    lot.setStatus(lot.getStatus());
                    lot.setCurrentPrice(lot.getAuction().getCurrentPrice());
                    return lot;
                })
                .map(lot ->  convertToLotResponse(lot))
                .collect(Collectors.toList());
        return lotResponses;
    }


    public LotResponse createLot(LotRequest lotRequest, String username) {
        User user = userRepository.findByEmail(username).orElseThrow(() -> new RuntimeException("User not found"));
        Auction auction = auctionRepository.findById(lotRequest.getAuctionId()).orElseThrow(() -> new RuntimeException("Auction not found"));

        Lot lot = Lot.builder()
                .auction(auction)
                .logo(auction.getLogo())
                .status("Ожидается")
                .name(lotRequest.getName())
                .title(lotRequest.getTitle())
                .description(lotRequest.getDescription())
                .timer(lotRequest.getTimer())
                .startingPrice(auction.getStartingPrice())
                .bet(lotRequest.getBet())
                .currentPrice(auction.getCurrentPrice())
                .endTime(auction.getAuctionStartTime().plusHours(1))
                .lotOwner(user)
                .build();

        Lot savedLot = lotsRepository.save(lot);

        return convertToLotResponse(savedLot);
    }

    public LotResponse getLotById(Long id) {
        Lot lot = lotsRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Lot not found with id: " + id));

        lot.setStatus(lot.getStatus());
        lot.setCurrentPrice(lot.getAuction().getCurrentPrice());
        lotsRepository.save(lot);
        LotResponse lotResponse = convertToLotResponse(lot);
        return lotResponse;
    }


    public LotResponse updateLot(Long id, LotRequest lotDetails, String username) {
        Lot lot = lotsRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Auction not found with id: " + id));
        User user = userRepository.findByEmail(username).orElseThrow(() -> new RuntimeException("User not found"));

        if (user.equals(lot.getLotOwner()) || user.getRole().equals(Role.ADMIN)) {
            final Lot updatedLot = getUpdatedLot(lotDetails, lot);
            Lot savedLot = lotsRepository.save(updatedLot);
            return convertToLotResponse(savedLot);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Auction with id: " + id + " can be modified by you!" );
        }

    }

    private Lot getUpdatedLot(LotRequest lotDetails, Lot lot) {
        lot.setName(lotDetails.getName());
        lot.setTitle(lotDetails.getTitle());
        lot.setTimer(lotDetails.getTimer());
        lot.setBet(lotDetails.getBet());
        return lot;
    }


    public void deleteAuction(Long id, String username) {
        User user = userRepository.findByEmail(username).orElseThrow(() -> new RuntimeException("User not found"));
        Lot lot = lotsRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Lot not found with id: " + id));

        if (user.getRole().equals(Role.ADMIN) || lot.getLotOwner().equals(user)) {
            lotsRepository.delete(lot);
        }
    }


    protected LotResponse convertToLotResponse(Lot lot) {
        LotResponse lotResponse = LotResponse.builder()
                .id(lot.getId())
                .logo(lot.getLogo())
                .auctionId(lot.getAuction().getId())
                .status(lot.getStatus())
                .owner(lot.getLotOwner().getShortUsername())
                .name(lot.getName())
                .title(lot.getTitle())
                .description(lot.getDescription())
                .timer(lot.getTimer())
                .startingPrice(lot.getStartingPrice())
                .bet(lot.getBet())
                .currentPrice(lot.getCurrentPrice())
                .endTime(lot.getEndTime())
                .build();
        return lotResponse;
    }
}
