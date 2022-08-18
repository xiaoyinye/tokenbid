package com.tokenbid.utils;

import com.tokenbid.controllers.AuctionController;
import com.tokenbid.models.Auction;
import com.tokenbid.models.Bid;
import com.tokenbid.services.AuctionService;
import com.tokenbid.services.BidService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.*;

@Component
public class TimerUtil {
    private final List<Auction> endingAuctions = new ArrayList<>();
    private final Timer databaseTimer = new Timer("DatabaseTimer");
    private final Timer auctionTimer = new Timer("AuctionTimer");
    private static final long databaseTimerDelay = 10L * 1000L;     // 10sec
    private static final long auctionTimerDelay = 1000L;            // 1sec

    private static Logger logger = LogManager.getLogger(AuctionController.class.getName());
    /**
     * Sending email to the user
     * @param to emailId Of receiver
     * @param subject subject of email
     * @param body body of email
     */
    @Autowired
    private AuctionService auctionService;

    @Autowired
    private BidService bidService;

    private TimerUtil() { }

    @EventListener(ApplicationReadyEvent.class)
    public void startTimer(ApplicationReadyEvent event) {
        startDatabaseTimer();
        startAuctionTimer();
    }

    /**
     * Query database every hour and add auctions ending in the upcoming hour to endingAuctions
     */
    private void startDatabaseTimer() {
        logger.debug("Scanning database to create list of upcoming auctions:");
        databaseTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                List<Auction> upcomingAuctions = auctionService.getAuctionsEndingInOneHour();
                for (Auction auction : upcomingAuctions) {
                    if (endingAuctions.stream().noneMatch(a -> a.getAuctionId() == auction.getAuctionId())) {
                        endingAuctions.add(auction);
                    }
                }
            }
        }, 0, databaseTimerDelay);
    }

    /**
     * Check list of ending auctions every minute and resolve those that have ended
     */
    private void startAuctionTimer() {
        logger.debug("Scanning list of auction to be ended in next hour:");
        auctionTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                ListIterator<Auction> itr = endingAuctions.listIterator();
                while (itr.hasNext()) {
                    Auction auction = itr.next();
                    if (auction.getStatus().equalsIgnoreCase("In Progress") && auction.getEndTime().before(Timestamp.from(Instant.now()))) {
                        int startingBid = auction.getStartingBid();
                        Bid bid = bidService.getHighestBidForAnAuction(auction.getAuctionId());
                        if (bid != null && bid.getBid() >= startingBid)
                            auction.setStatus("Sold");
                        else
                            auction.setStatus("Not Sold");
                        auctionService.update(auction);
                        itr.remove();
                    }
                }
            }
        }, auctionTimerDelay/2, auctionTimerDelay);
    }
}
