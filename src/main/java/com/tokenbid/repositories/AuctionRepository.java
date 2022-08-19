package com.tokenbid.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.tokenbid.models.Auction;

import java.util.List;

@Repository
public interface AuctionRepository extends JpaRepository<Auction, Integer> {

    /**
     * @return A list of auctions that have an end time less than half an hour from
     *         now
     */
    @Query(value = "SELECT * FROM auctions WHERE status = 'In Progress' AND end_time < (NOW() + INTERVAL '0.5 hour') ORDER BY end_time ASC", nativeQuery = true)
    List<Auction> findAuctionsEndingInNextHalfAnHour();

    /**
     * @return A list of auctions that have an 'In Progress' status and haven't
     *         ended
     */
    @Query(value = "SELECT * FROM auctions WHERE status = 'In Progress' AND end_time > NOW() ORDER BY end_time ASC", nativeQuery = true)
    List<Auction> findAllActiveAuctions();
}
