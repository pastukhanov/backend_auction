package org.luxeloot.backend.controllers;


import org.luxeloot.backend.config.JwtService;
import org.luxeloot.backend.dto.AuctionRequest;
import org.luxeloot.backend.dto.AuctionResponse;
import org.luxeloot.backend.dto.LotResponse;
import org.luxeloot.backend.services.AuctionService;
import org.luxeloot.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/api/v1/auctions")
public class AuctionsController {

    @Autowired
    private AuctionService auctionService;

    @Autowired
    private JwtService jwtService;

    // POST method to add a new auction
    @PostMapping
    public ResponseEntity<AuctionResponse> createAuction(@RequestBody AuctionRequest auction, @RequestHeader("Authorization") String token) {
        String actualToken = token.split("\\s+")[1];
        String userName = jwtService.extractUsername(actualToken);
        AuctionResponse savedAuction = auctionService.createAuction(auction, userName);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedAuction);
    }

    // GET method to retrieve all auctions
    @GetMapping
    public List<AuctionResponse> getAllAuctions() {
        return auctionService.getAllAuctions();
    }

    @GetMapping("/favorites")
    public List<AuctionResponse> getFavoriteAuctionsByUser(@RequestHeader("Authorization") String token)  {
        String actualToken = token.split("\\s+")[1];
        String userName = jwtService.extractUsername(actualToken);
        return auctionService.getFavoriteAuctionsByUser(userName);
    }

    // GET method to retrieve a single auction by ID
    @GetMapping("/{id}")
    public ResponseEntity<AuctionResponse> getAuctionById(@PathVariable Long id) {
        return ResponseEntity.ok(auctionService.getAuctionById(id));
    }

    // GET method to retrieve all lots associated with an auction
    @GetMapping("/{id}/lots")
    public List<LotResponse> getLotsByAuctionById(@PathVariable Long id) {
        return auctionService.getLotsByAuctionById(id);
    }

    // PUT method to update an auction
    @PutMapping("/{id}")
    public ResponseEntity<AuctionResponse> updateAuction(@PathVariable Long id,
                                                         @RequestBody AuctionRequest auctionDetails,
                                                         @RequestHeader("Authorization") String token) {
        String actualToken = token.split("\\s+")[1];
        String userName = jwtService.extractUsername(actualToken);
        return ResponseEntity.ok(auctionService.updateAuction(id, auctionDetails, userName));
    }

    // DELETE method to delete an auction
    @PostMapping("/{id}/delete")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteAuction(@PathVariable Long id) {
        auctionService.deleteAuction(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/favorites/{auctionId}")
    public ResponseEntity<?> addFavorite(@PathVariable Long auctionId, @RequestHeader("Authorization") String token) {
        try {
            String actualToken = token.split("\\s+")[1];
            String userName = jwtService.extractUsername(actualToken);
            auctionService.addAuctionToFavorites(userName, auctionId);
            return ResponseEntity.ok("Auction added to favorites");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error adding auction to favorites: " + e.getMessage());
        }
    }

    @DeleteMapping("/favorites/{auctionId}")
    public ResponseEntity<?> removeFavorite(@PathVariable Long auctionId, @RequestHeader("Authorization") String token) {
        try {
            String actualToken = token.split("\\s+")[1];
            String userName = jwtService.extractUsername(actualToken);
            auctionService.removeAuctionFromFavorites(userName, auctionId);
            return ResponseEntity.ok("Auction removed from favorites");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error removing auction from favorites: " + e.getMessage());
        }
    }
}

