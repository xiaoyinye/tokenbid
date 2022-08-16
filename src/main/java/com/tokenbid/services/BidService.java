package com.tokenbid.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tokenbid.models.Bid;
import com.tokenbid.repositories.BidRepository;

@Service
public class BidService implements IService<Bid> {
    private BidRepository bidRepository;

    @Autowired
    public BidService(BidRepository bidRepository) {
        this.bidRepository = bidRepository;
    }

    /**
     * Add a new bid if newBid is higher than the current highest bid for the same auction
     * @param newBid The item to be saved.
     * @return id of the newly added bid
     * @throws IllegalArgumentException if the new bid is lower than the current highest bid
     */
    @Override
    public int add(Bid newBid) throws IllegalArgumentException {
        Bid currentHighest = getHighestBidForAnAuction(newBid.getAuctionId());
        if (newBid.getBid() <= currentHighest.getBid())
            throw new IllegalArgumentException("Bid must be higher than the current highest bid of " + currentHighest.getBid() + " tokens");
        newBid.setOutbid(true);
        currentHighest.setOutbid(false);
        update(currentHighest);
        return bidRepository.save(newBid).getBidId();
    }

    @Override
    public void update(Bid item) {
        if (bidRepository.findById(item.getBidId()).isPresent()) {
            bidRepository.save(item);
        }
    }

    @Override
    public void delete(int id) {
        if (bidRepository.findById(id).isPresent()) {
            bidRepository.deleteById(id);
        }
    }

    @Override
    public Bid getById(int id) {
        if (bidRepository.findById(id).isPresent()) {
            return bidRepository.findById(id).get();
        }

        return null;
    }

    @Override
    public List<Bid> getAll() {
        return bidRepository.findAll();
    }

    /**
     * find the highest bid and user id from bids table
     * @param auctionID
     * @return Bid object with the highest Bid
     */
    public Bid getHighestBidForAnAuction(int auctionID){
        return bidRepository.getHighestBidForAnAuction(auctionID);
    }
}
