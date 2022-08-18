package com.tokenbid.controllers;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tokenbid.models.Auction;
import com.tokenbid.services.AuctionService;

@RestController
@RequestMapping("/auctions")
public class AuctionController implements IController<Auction> {
    private AuctionService auctionService;

    private static Logger logger = LogManager.getLogger(AuctionController.class.getName());

    @Autowired
    public AuctionController(AuctionService auctionService) {
        this.auctionService = auctionService;
    }

    @Override
    @PostMapping(path = "/add", consumes = "application/json")
    public ResponseEntity<String> add(@RequestBody Auction auction) throws URISyntaxException {
        return ResponseEntity.created(new URI("/auctions/" + auctionService.add(auction))).build();
    }

    @Override
    @PutMapping(path = "/{id}", consumes = "application/json")
    public ResponseEntity<String> update(@PathVariable("id") int id, @RequestBody Auction updatedAuction) {
        logger.debug("updating an auction with auction Id: "+id);
        if (auctionService.getById(id) != null) {
            updatedAuction.setAuctionId(id);
            auctionService.update(updatedAuction);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @Override
    @DeleteMapping(path = "/{id}")
    public ResponseEntity<String> delete(@PathVariable("id") int id) {
        logger.debug("Deleting the auction with auctionID: " +id);
        if (auctionService.getById(id) != null) {
            auctionService.delete(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @Override
    @GetMapping(path = "/{id}")
    public ResponseEntity<Auction> get(@PathVariable("id") int id) {
        Auction auction = auctionService.getById(id);
        if (auction != null) {
            return ResponseEntity.ok(auction);
        }

        return ResponseEntity.notFound().build();
    }

    @Override
    @GetMapping(path = "/all", produces = "application/json")
    public ResponseEntity<List<Auction>> getAll() {
        return ResponseEntity.ok(auctionService.getAll());
    }

    /**
     * To get active auctions from the database
     * @return List of active auctions
     */
    @GetMapping(path = "/active", produces = "application/json")
    public ResponseEntity<List<Auction>> getActive() {
        logger.debug("Retrieving list of active auctions ");
        return ResponseEntity.ok(auctionService.getActive()); }

}
