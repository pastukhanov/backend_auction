package org.luxeloot.backend.services;

import jakarta.transaction.Transactional;
import org.luxeloot.backend.dto.AuctionRequest;
import org.luxeloot.backend.dto.LotResponse;
import org.luxeloot.backend.models.Auction;
import org.luxeloot.backend.models.Lot;
import org.luxeloot.backend.repository.AuctionRepository;
import org.luxeloot.backend.dto.AuctionResponse;
import org.luxeloot.backend.models.Role;
import org.luxeloot.backend.models.User;
import org.luxeloot.backend.repository.UserRepository;
import org.luxeloot.backend.dto.UserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class AuctionService {

    @Autowired
    private AuctionRepository auctionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LotsService lotsService;

    public AuctionResponse getAuctionById(Long id) {
        Auction auction = auctionRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Auction not found with id: " + id));
        auction.setStatus(auction.getStatus());
        auctionRepository.save(auction);
        AuctionResponse auctionResponse = getAuctionResponse(auction);
        return auctionResponse;
    }

    private static AuctionResponse getAuctionResponse(Auction auction) {
        UserResponse user = UserResponse.builder()
                .id(auction.getUser().getId())
                .firstname(auction.getUser().getFirstname())
                .lastname(auction.getUser().getLastname())
                .email(auction.getUser().getEmail())
                .role(auction.getUser().getRole())
                .build();

        AuctionResponse auctionResponse = AuctionResponse.builder()
                .id(auction.getId())
                .title(auction.getTitle())
                .description(auction.getDescription())
                .auctionStartTime(auction.getAuctionStartTime())
                .startingPrice(auction.getStartingPrice())
                .logo(auction.getLogo())
                .status(auction.getStatus())
                .topic(auction.getTopic())
                .currentPrice(auction.getCurrentPrice())
                .lotsTimer(auction.getLotsTimer())
                .authors(auction.getAuthors())
                .user(user)
                .lotsCount(auction.getLotsCount())
                .build();
        return auctionResponse;
    }

    public AuctionResponse updateAuction(Long id, AuctionRequest auctionDetails, String username) {
        Auction auction = auctionRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Auction not found with id: " + id));
        User user = userRepository.findByEmail(username).orElseThrow(() -> new RuntimeException("User not found"));

        if (user.equals(auction.getUser()) || user.getRole().equals(Role.ADMIN)) {
            final Auction updatedAuction = getUpdatedAuction(auctionDetails, auction);
            return getAuctionResponse(updatedAuction);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Auction with id: " + id + " can be modified by you!" );
        }

    }

    public void deleteAuction(Long id) {
        Auction auction = auctionRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Auction not found with id: " + id));
        auctionRepository.delete(auction);
    }

    public AuctionResponse createAuction(AuctionRequest auction, String username) {
        User user = userRepository.findByEmail(username).orElseThrow(() -> new RuntimeException("User not found"));

        Auction tmpAuction = Auction.builder()
                .title(auction.getTitle())
                .description(auction.getDescription())
                .auctionStartTime(auction.getAuctionStartTime())
                .startingPrice(auction.getStartingPrice())
                .logo(auction.getLogo())
                .authors(auction.getAuthors())
                .topic(auction.getTopic())
                .lotsTimer(auction.getLotsTimer())
                .user(user)
                .build();

        tmpAuction.setStatus(tmpAuction.getStatus());

        Auction savedAuction = auctionRepository.save(tmpAuction);
        return getAuctionResponse(savedAuction);
    }

    public List<AuctionResponse> getAllAuctions() {
        List<Auction> auctions = auctionRepository.findAll();
        List<AuctionResponse> auctionResponses = auctions
                .stream()
                .map(auction ->  getAuctionResponse(auction))
                .map(auction -> {
                    auction.setStatus(auction.getStatus());
                    auction.setCurrentPrice(auction.getCurrentPrice());
                    return auction;
                })
                .collect(Collectors.toList());
        return auctionResponses;
    }

    public List<AuctionResponse> getFavoriteAuctionsByUser(String username) {
        User user = userRepository.findByEmail(username).orElseThrow(() -> new RuntimeException("User not found"));
        List<Auction> auctions = user.getFavoriteAuctions();
        List<AuctionResponse> auctionResponses = auctions
                .stream()
                .map(auction ->  getAuctionResponse(auction))
                .map(auction -> {
                    auction.setStatus(auction.getStatus());
                    auction.setCurrentPrice(auction.getCurrentPrice());
                    return auction;
                })
                .collect(Collectors.toList());
        return auctionResponses;
    }

    private Auction getUpdatedAuction(AuctionRequest auctionDetails, Auction auction) {
        auction.setTitle(auctionDetails.getTitle());
        auction.setLogo(auctionDetails.getLogo());
        auction.setStatus(auctionDetails.getStatus());
        auction.setTopic(auctionDetails.getTopic());
        auction.setAuthors(auctionDetails.getAuthors());
        auction.setLotsTimer(auctionDetails.getLotsTimer());
        auction.setAuctionStartTime(auctionDetails.getAuctionStartTime());
        auction.setDescription(auctionDetails.getDescription());

        final Auction updatedAuction = auctionRepository.save(auction);
        return updatedAuction;
    }

    @Transactional
    public void addAuctionToFavorites(Long userId, Long auctionId) {
        User user = userRepository.findUserById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Auction auction = auctionRepository.findById(auctionId).orElseThrow(() -> new RuntimeException("Auction not found"));

        List<Auction> favorites = user.getFavoriteAuctions();
        if (!favorites.contains(auction)) {
            favorites.add(auction);
            userRepository.save(user);
        }
    }

    public void addAuctionToFavorites(String username, Long auctionId) {
        User user = userRepository.findByEmail(username).orElseThrow(() -> new RuntimeException("User not found"));
        addAuctionToFavorites(user.getId(), auctionId);
    }

    @Transactional
    public void removeAuctionFromFavorites(Long userId, Long auctionId) {
        User user = userRepository.findUserById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Auction auction = auctionRepository.findById(auctionId).orElseThrow(() -> new RuntimeException("Auction not found"));

        List<Auction> favorites = user.getFavoriteAuctions();
        if (favorites.contains(auction)) {
            favorites.remove(auction);
            userRepository.save(user);
        }
    }

    public void removeAuctionFromFavorites(String username, Long auctionId) {
        User user = userRepository.findByEmail(username).orElseThrow(() -> new RuntimeException("User not found"));
        removeAuctionFromFavorites(user.getId(), auctionId);
    }

    public List<LotResponse> getLotsByAuctionById(Long auctionId) {
        Auction auction = auctionRepository.findById(auctionId).orElseThrow(() -> new RuntimeException("Auction not found"));
        List<LotResponse> lots = auction.getLots().stream()
                .map(a -> lotsService.convertToLotResponse(a))
                .sorted(Comparator.comparingDouble(lot -> -1 * (Math.abs(lot.getBet() - lot.getStartingPrice()))))
                .collect(Collectors.toList());
        return lots;
    }
}
