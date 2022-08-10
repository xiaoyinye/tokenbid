package com.tokenbid.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tokenbid.models.Auction;

@Repository
public interface AuctionRepository extends JpaRepository<Auction, Integer> {
}
