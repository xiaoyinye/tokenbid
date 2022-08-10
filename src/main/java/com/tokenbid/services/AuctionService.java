package com.tokenbid.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tokenbid.models.Auction;
import com.tokenbid.repositories.AuctionRepository;

@Service
public class AuctionService implements IService<Auction> {
    private AuctionRepository auctionRepository;

    @Autowired
    public AuctionService(AuctionRepository auctionRepository) {
        this.auctionRepository = auctionRepository;
    }

    @Override
    public int add(Auction item) {
        return auctionRepository.save(item).getAuctionId();
    }

    @Override
    public void update(Auction item) {
        if (auctionRepository.findById(item.getAuctionId()).isPresent()) {
            auctionRepository.save(item);
        }
    }

    @Override
    public void delete(int id) {
        if (auctionRepository.findById(id).isPresent()) {
            auctionRepository.deleteById(id);
        }
    }

    @Override
    public Auction getById(int id) {
        if (auctionRepository.findById(id).isPresent()) {
            return auctionRepository.findById(id).get();
        }

        return null;
    }

    @Override
    public List<Auction> getAll() {
        return auctionRepository.findAll();
    }

    public void testMessage() {
        System.out.println("AuctionService.testMessage()");
    }
}
