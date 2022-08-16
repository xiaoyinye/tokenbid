package com.tokenbid.services;

import com.tokenbid.models.Auction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

@Component
public class TimerService {
    private final List<Auction> endingAuctions = new ArrayList<>();
    private final Timer databaseTimer = new Timer("DatabaseTimer");
    private final Timer auctionTimer = new Timer("AuctionTimer");
    private static final long databaseTimerDelay = 60L * 60L * 1000L;   // 1hr
    private static final long auctionTimerDelay = 60L * 1000L;          // 1min

    @Autowired
    private AuctionService auctionService;

    private TimerService() { }

    @EventListener(ApplicationReadyEvent.class)
    public void startTimer(ApplicationReadyEvent event) {
        
        startDatabaseTimer();
        startAuctionTimer();

//        System.out.println("Application ready, startTimer called");
//        Timer timer = new Timer("Timer");
//        timer.scheduleAtFixedRate(new TimerTask() {
//            @Override
//            public void run() {
//                System.out.println("Attempting to execute run");
//                Auction auction = auctionService.getEarliestExpiredAuction();
//                if (auction != null) {
//                    System.out.println(auction.getAuctionId());
//                }
//            }
//        }, 1000L, 1000L);
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
        }, databaseTimerDelay);
    }

    /**
     * Check list of ending auctions every minute and resolve those that have ended
     */
    private void startAuctionTimer() {
        auctionTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                for (Auction auction : endingAuctions) {
                    if (auction.getEndTime().before(Timestamp.from(Instant.now()))) {
                        // resolve auction

                    }
                }
            }
        }, auctionTimerDelay);
    }
}
