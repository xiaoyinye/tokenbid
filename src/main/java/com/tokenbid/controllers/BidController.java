package com.tokenbid.controllers;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tokenbid.models.Bid;
import com.tokenbid.services.BidService;

@RestController
@RequestMapping("/bids")
public class BidController implements IController<Bid> {
    private BidService bidService;
    private static Logger logger = LogManager.getLogger(AuctionController.class.getName());

    @Autowired
    public BidController(BidService bidService) {
        this.bidService = bidService;
    }

    @Override
    @PostMapping(path = "/add", consumes = "application/json")
    public ResponseEntity<String> add(@RequestBody Bid bid) throws URISyntaxException {
        try {
            logger.debug("adding a new bid");
            return ResponseEntity.created(new URI("/bids/" + bidService.add(bid))).build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @Override
    @PutMapping(path = "/{id}", consumes = "application/json")
    public ResponseEntity<String> update(@PathVariable("id") int id, @RequestBody Bid updatedBid) {
        logger.debug("Updating a bid with bid ID:" +id);
        if (bidService.getById(id) != null) {
            updatedBid.setBidId(id);
            bidService.update(updatedBid);
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.notFound().build();
    }

    @Override
    @DeleteMapping(path = "/{id}")
    public ResponseEntity<String> delete(@PathVariable("id") int id) {
        logger.debug("deleting a bid with id: "+id);
        if (bidService.getById(id) != null) {
            bidService.delete(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @Override
    @GetMapping(path = "/{id}", produces = "application/json")
    public ResponseEntity<Bid> get(@PathVariable("id") int id) {
        Bid bid = bidService.getById(id);
        if (bid != null) {
            return ResponseEntity.ok(bid);
        }

        return ResponseEntity.notFound().build();
    }

    @Override
    @GetMapping(path = "/all", produces = "application/json")
    public ResponseEntity<List<Bid>> getAll() {
        return ResponseEntity.ok(bidService.getAll());
    }

    /**
     * To get the highest bid and corresponding user id by auctionID
     * 
     * @param auctionId
     * @return Bid object with the highest bid and user id as per Auction ID
     */
    @GetMapping(path = "/highest-bid/{id}", produces = "application/json")
    public ResponseEntity<Bid> getHighestBidAndUserId(@PathVariable("id") int auctionId) {
        logger.debug("getting highest bid and user ID for auctoin ID: " +auctionId);
        Bid bid = bidService.getHighestBidForAnAuction(auctionId);
        if (bid != null) {
            return ResponseEntity.ok(bid);
        }
        return ResponseEntity.notFound().build();
    }
}
