package com.tokenbid.services;

import java.util.List;

import com.tokenbid.controllers.AuctionController;
import com.tokenbid.models.Auction;
import com.tokenbid.models.Item;
import com.tokenbid.models.User;
import com.tokenbid.repositories.AuctionRepository;
import com.tokenbid.repositories.ItemRepository;
import com.tokenbid.repositories.UserRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tokenbid.models.Bid;
import com.tokenbid.repositories.BidRepository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BidService implements IService<Bid> {
    private BidRepository bidRepository;
    private AuctionRepository auctionRepository;
    private ItemRepository itemRepository;
    private UserRepository userRepository;

    private static Logger logger = LogManager.getLogger(AuctionController.class.getName());

    @Autowired
    public BidService(BidRepository bidRepository, AuctionRepository auctionRepository, ItemRepository itemRepository, UserRepository userRepository) {
        this.bidRepository = bidRepository;
        this.auctionRepository = auctionRepository;
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
    }

    /**
     * Add a new bid if newBid is higher than the current highest bid for the same auction
     * @param newBid The item to be saved.
     * @return id of the newly added bid
     * @throws IllegalArgumentException if the new bid is lower than the current highest bid or if a user tries to bid on their own item
     */
    @Override
    public int add(Bid newBid) throws IllegalArgumentException {
        logger.debug("Attempting to add a new bid");
        Auction auction = null;
        Item item = null;
        User newBidUser = null;
        if (auctionRepository.findById(newBid.getAuctionId()).isPresent())
            auction = auctionRepository.findById(newBid.getAuctionId()).get();
        else throw new IllegalArgumentException("Auction not found");
        if (itemRepository.findById(auction.getItemId()).isPresent())
            item = itemRepository.findById(auction.getItemId()).get();
        else throw new IllegalArgumentException("Item not found");
        if (userRepository.findById(newBid.getUserId()).isPresent())
            newBidUser = userRepository.findById(newBid.getUserId()).get();
        else throw new IllegalArgumentException("User not found");

        int startingBid = auction.getStartingBid();
        Bid currentHighest = getHighestBidForAnAuction(newBid.getAuctionId());

        // Check that the user is not bidding on their own item
        if (newBid.getUserId() == item.getUserId())
            throw new IllegalArgumentException("User cannot bid on their own item");

        // Check that the bid is higher than the current highest bid
        if (currentHighest != null) {
            if (newBid.getBid() <= currentHighest.getBid())
                throw new IllegalArgumentException("Bid must be higher than the current highest bid of " + currentHighest.getBid() + " tokens");
        } else if (newBid.getBid() < startingBid)
            throw new IllegalArgumentException("Bid must be higher than the starting bid of " + startingBid + " tokens");

        // Check that the user has enough tokens
        if (currentHighest != null && newBidUser.getUserId() == currentHighest.getUserId()) {
            // new bid is placed by the same user who placed the previous highest bid
            if (newBid.getBid() > (currentHighest.getBid() + newBidUser.getTokens()))
                throw new IllegalArgumentException("User does not have enough tokens");
        } else {
            // new bid is placed by a different user from the previous highest bid
            if (newBid.getBid() > newBidUser.getTokens())
                throw new IllegalArgumentException("User does not have enough tokens");
        }

        // Add back tokens to user with the previous highest bid
        if (currentHighest != null) {
            currentHighest.setOutbid(false);
            update(currentHighest);
            if (userRepository.findById(currentHighest.getUserId()).isPresent()) {
                User prevUser = userRepository.findById(currentHighest.getUserId()).get();
                prevUser.setTokens(prevUser.getTokens() + currentHighest.getBid());
            }
        }

        // Remove tokens from user submitting the new bid
        newBid.setOutbid(true);
        newBidUser.setTokens(newBidUser.getTokens() - newBid.getBid());
        logger.debug("New Bid added for the item: " +item.getItemId());
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

    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteForAuction(int auctionId) {
        bidRepository.deleteAllBidsForAnAuction(auctionId);
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
        logger.debug("Getting the highest bid for auction: " +auctionID);
        return bidRepository.getHighestBidForAnAuction(auctionID);
    }
}
