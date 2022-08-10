package com.tokenbid.models;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "auctions")
public class Auction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int auctionId;

    @Column(name = "item_id")
    private int itemId;

    @Column(name = "starting_bid")
    private int startingBid;

    @Column(name = "current_bid")
    private int currentBid;

    @Column(name = "previous_bid")
    private int previousBid;

    @Column(name = "status")
    private String status;

    @Column(name = "start_time")
    private Timestamp startTime;

    @Column(name = "end_time")
    private Timestamp endTime;

    /**
     * Default constructor.
     */
    public Auction() {
    }

    /**
     * Parameterized constructor for Auction class.
     * 
     * @param itemId      The item id of the auction.
     * @param startingBid The starting bid of the auction.
     * @param startTime   The start time of the auction.
     * @param endTime     The end time of the auction.
     */
    public Auction(int itemId, int startingBid, Timestamp startTime, Timestamp endTime) {
        this.itemId = itemId;
        this.startingBid = startingBid;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    /**
     * Parameterized constructor for Auction class.
     * 
     * @param itemId      The item id of the auction.
     * @param startingBid The starting bid of the auction.
     * @param currentBid  The current bid of the auction.
     * @param previousBid The previous bid of the auction.
     * @param status      The status of the auction.
     * @param startTime   The start time of the auction.
     * @param endTime     The end time of the auction.
     */
    public Auction(int itemId, int startingBid, int currentBid, int previousBid, String status, Timestamp startTime,
            Timestamp endTime) {
        this.itemId = itemId;
        this.startingBid = startingBid;
        this.currentBid = currentBid;
        this.previousBid = previousBid;
        this.status = status;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    /**
     * Parameterized constructor for Auction class.
     * 
     * @param auctionId   The auction id of the auction.
     * @param itemId      The item id of the auction.
     * @param startingBid The starting bid of the auction.
     * @param currentBid  The current bid of the auction.
     * @param previousBid The previous bid of the auction.
     * @param status      The status of the auction.
     * @param startTime   The start time of the auction.
     * @param endTime     The end time of the auction.
     */
    public Auction(int auctionId, int itemId, int startingBid, int currentBid, int previousBid, String status,
            Timestamp startTime, Timestamp endTime) {
        this.auctionId = auctionId;
        this.itemId = itemId;
        this.startingBid = startingBid;
        this.currentBid = currentBid;
        this.previousBid = previousBid;
        this.status = status;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    /**
     * Gets the auction id of the auction.
     * 
     * @return The auction id of the auction.
     */
    public int getAuctionId() {
        return auctionId;
    }

    /**
     * Sets the auction id of the auction.
     * 
     * @param auctionId The auction id of the auction.
     */
    public void setAuctionId(int auctionId) {
        this.auctionId = auctionId;
    }

    /**
     * Gets the item id of the auction.
     * 
     * @return The item id of the auction.
     */
    public int getItemId() {
        return itemId;
    }

    /**
     * Sets the item id of the auction.
     * 
     * @param itemId The item id of the auction.
     */
    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    /**
     * Gets the starting bid of the auction.
     * 
     * @return The starting bid of the auction.
     */
    public int getStartingBid() {
        return startingBid;
    }

    /**
     * Sets the starting bid of the auction.
     * 
     * @param startingBid The starting bid of the auction.
     */
    public void setStartingBid(int startingBid) {
        this.startingBid = startingBid;
    }

    /**
     * Gets the current bid of the auction.
     * 
     * @return The current bid of the auction.
     */
    public int getCurrentBid() {
        return currentBid;
    }

    /**
     * Sets the current bid of the auction.
     * 
     * @param currentBid The current bid of the auction.
     */
    public void setCurrentBid(int currentBid) {
        this.currentBid = currentBid;
    }

    /**
     * Gets the previous bid of the auction.
     * 
     * @return The previous bid of the auction.
     */
    public int getPreviousBid() {
        return previousBid;
    }

    /**
     * Sets the previous bid of the auction.
     * 
     * @param previousBid The previous bid of the auction.
     */
    public void setPreviousBid(int previousBid) {
        this.previousBid = previousBid;
    }

    /**
     * Gets the status of the auction.
     * 
     * @return The status of the auction.
     */
    public String getStatus() {
        return status;
    }

    /**
     * Sets the status of the auction.
     * 
     * @param status The status of the auction.
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Gets the start time of the auction.
     * 
     * @return The start time of the auction.
     */
    public Timestamp getStartTime() {
        return startTime;
    }

    /**
     * Sets the start time of the auction.
     * 
     * @param startTime The start time of the auction.
     */
    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    /**
     * Gets the end time of the auction.
     * 
     * @return The end time of the auction.
     */
    public Timestamp getEndTime() {
        return endTime;
    }

    /**
     * Sets the end time of the auction.
     * 
     * @param endTime The end time of the auction.
     */
    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return "Auction [auctionId=" + auctionId +
                ", itemId=" + itemId +
                ", startingBid=" + startingBid +
                ", currentBid=" + currentBid +
                ", previousBid=" + previousBid +
                ", status=" + status +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                "]";
    }
}
