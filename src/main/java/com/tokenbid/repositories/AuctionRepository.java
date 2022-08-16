package com.tokenbid.repositories;

import com.tokenbid.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tokenbid.models.Auction;

import java.util.List;

@Repository
public interface AuctionRepository extends JpaRepository<Auction, Integer> {
    /**
     * Finds the auction with the earliest end time
     *
     * @return Auction with the earliest end time
     */
    @Query(value = "SELECT * FROM auctions ORDER BY end_time ASC LIMIT 1;", nativeQuery = true)
    Auction findByEarliestEndTime();

    @Query(value = "SELECT * FROM auctions WHERE end_time < (NOW() + INTERVAL '1 hour') ORDER BY end_time ASC")
    List<Auction> findAuctionsEndingInNextHour();
}
