package com.tokenbid.services;

import com.tokenbid.models.Auction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.Timer;
import java.util.TimerTask;

@Component
public class TimerService {
    private static final long delay = 1000L;
    private static final long period = 1000L;

    @Autowired
    private AuctionService auctionService;

    private TimerService() { }

    @EventListener(ApplicationReadyEvent.class)
    public void startTimer(ApplicationReadyEvent event) {
        System.out.println("Application ready, startTimer called");
        Timer timer = new Timer("AuctionTimer");
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                System.out.println("Attempting to execute run");
                Auction auction = auctionService.getEarliestExpiredAuction();
                if (auction != null) {
                    System.out.println(auction.getAuctionId());
                }
            }
        }, delay, period);
    }
}
