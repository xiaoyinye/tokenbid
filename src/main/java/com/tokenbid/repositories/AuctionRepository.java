package com.tokenbid.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tokenbid.models.Auction;

public interface AuctionRepository extends JpaRepository<Auction, Integer> {
}
