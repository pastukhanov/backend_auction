package org.luxeloot.backend.controllers;

import org.luxeloot.backend.config.JwtService;
import org.luxeloot.backend.dto.LotRequest;
import org.luxeloot.backend.dto.LotResponse;
import org.luxeloot.backend.services.LotsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/v1/lots")
public class LotsController {

    @Autowired
    private LotsService lotsService;

    @Autowired
    private JwtService jwtService;


    // Get a specific lot by ID
    @GetMapping
    public List<LotResponse> listAllLots(@RequestHeader("Authorization") String token) {
        String actualToken = token.split("\\s+")[1];
        String userName = jwtService.extractUsername(actualToken);
        return lotsService.getAllLots(userName);
    }

    // Get a specific lot by ID
    @GetMapping("/{id}")
    public LotResponse getLotById(@PathVariable Long id) {
        return lotsService.getLotById(id);
    }

    // Create a new lot
    @PostMapping
    public LotResponse createLot(@RequestBody LotRequest lot, @RequestHeader("Authorization") String token) {
        String actualToken = token.split("\\s+")[1];
        String userName = jwtService.extractUsername(actualToken);
        return lotsService.createLot(lot, userName);
    }

    // Update an existing lot
    @PutMapping("/{id}")
    public LotResponse updateLot(@PathVariable Long id, @RequestBody LotRequest updatedLot, @RequestHeader("Authorization") String token) {
        String actualToken = token.split("\\s+")[1];
        String userName = jwtService.extractUsername(actualToken);
        return lotsService.updateLot(id, updatedLot, userName);
    }

    // Delete a lot
    @PostMapping("/{id}/delete")
    public void deleteLot(@PathVariable Long id, @RequestHeader("Authorization") String token) {
        String actualToken = token.split("\\s+")[1];
        String userName = jwtService.extractUsername(actualToken);
        lotsService.deleteAuction(id, userName);
    }
}
