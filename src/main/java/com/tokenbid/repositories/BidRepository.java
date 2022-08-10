package com.tokenbid.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tokenbid.models.Bid;

public interface BidRepository extends JpaRepository<Bid, Integer> {
}
