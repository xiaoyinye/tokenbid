package com.tokenbid.services;

import com.tokenbid.models.Auction;
import com.tokenbid.models.Bid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.*;

@Component
public class TimerService {
    private final List<Auction> endingAuctions = new ArrayList<>();
    private final Timer databaseTimer = new Timer("DatabaseTimer");
    private final Timer auctionTimer = new Timer("AuctionTimer");
    private static final long databaseTimerDelay = 10L * 1000L;     // 10sec
    private static final long auctionTimerDelay = 1000L;            // 1sec

    @Autowired
    private AuctionService auctionService;

    @Autowired
    private BidService bidService;

    private TimerService() { }

    @EventListener(ApplicationReadyEvent.class)
    public void startTimer(ApplicationReadyEvent event) {
        startDatabaseTimer();
        startAuctionTimer();
    }

    /**
     * Query database every hour and add auctions ending in the upcoming hour to endingAuctions
     */
    private void startDatabaseTimer() {
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
