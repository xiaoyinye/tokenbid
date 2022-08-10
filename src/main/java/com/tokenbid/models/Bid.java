package com.tokenbid.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "bids")
public class Bid {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int bidId;

    @Column(name = "auction_id")
    private int auctionId;

    @Column(name = "user_id")
    private int userId;

    @Column(name = "bid")
    private int bid;

    @Column(name = "is_outbid")
    private boolean isOutbid;

    /**
     * Default constructor.
     */
    public Bid() {
    }

    /**
     * Parameterized constructor for Bid class.
     * 
     * @param auctionId The auction id of the bid.
     * @param userId    The user id of the bid.
     * @param bid       The bid of the bid.
     */
    public Bid(int auctionId, int userId, int bid) {
        this.auctionId = auctionId;
        this.userId = userId;
        this.bid = bid;
    }

    /**
     * Parameterized constructor for Bid class.
     * 
     * @param auctionId The auction id of the bid.
     * @param userId    The user id of the bid.
     * @param bid       The bid of the bid.
     * @param isOutbid  The is outbid of the bid.
     */
    public Bid(int auctionId, int userId, int bid, boolean isOutbid) {
        this.auctionId = auctionId;
        this.userId = userId;
        this.bid = bid;
        this.isOutbid = isOutbid;
    }

    /**
     * Getter for the bid id.
     * 
     * @return The bid id.
     */
    public int getBidId() {
        return bidId;
    }

    /**
     * Setter for the bid id.
     * 
     * @param bidId The bid id.
     */
    public void setBidId(int bidId) {
        this.bidId = bidId;
    }

    /**
     * Getter for the auction id.
     * 
     * @return The auction id.
     */
    public int getAuctionId() {
        return auctionId;
    }

    /**
     * Setter for the auction id.
     * 
     * @param auctionId The auction id.
     */
    public void setAuctionId(int auctionId) {
        this.auctionId = auctionId;
    }

    /**
     * Getter for the user id.
     * 
     * @return The user id.
     */
    public int getUserId() {
        return userId;
    }

    /**
     * Setter for the user id.
     * 
     * @param userId The user id.
     */
    public void setUserId(int userId) {
        this.userId = userId;
    }

    /**
     * Getter for the bid.
     * 
     * @return The bid.
     */
    public int getBid() {
        return bid;
    }

    /**
     * Setter for the bid.
     * 
     * @param bid The bid.
     */
    public void setBid(int bid) {
        this.bid = bid;
    }

    /**
     * Getter for the is outbid.
     * 
     * @return The is outbid.
     */
    public boolean isOutbid() {
        return isOutbid;
    }

    /**
     * Setter for the is outbid.
     * 
     * @param isOutbid The is outbid.
     */
    public void setOutbid(boolean isOutbid) {
        this.isOutbid = isOutbid;
    }

    @Override
    public String toString() {
        return "Bid [bidId=" + bidId +
                ", auctionId=" + auctionId +
                ", userId=" + userId +
                ", bid=" + bid +
                ", isOutbid=" + isOutbid +
                "]";
    }
}
