package com.tokenbid.services;

import com.tokenbid.models.Auction;
import com.tokenbid.models.Bid;
import com.tokenbid.models.Item;
import com.tokenbid.models.User;
import com.tokenbid.repositories.AuctionRepository;
import com.tokenbid.repositories.BidRepository;
import com.tokenbid.repositories.ItemRepository;
import com.tokenbid.repositories.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;

class BidServiceTest {
    private BidRepository bidRepository = Mockito.mock(BidRepository.class);
    private AuctionRepository auctionRepository = Mockito.mock(AuctionRepository.class);
    private ItemRepository itemRepository = Mockito.mock(ItemRepository.class);
    private UserRepository userRepository = Mockito.mock(UserRepository.class);
    private BidService bidService;

    @BeforeEach
    public void initBefore() {
        bidService = new BidService(bidRepository, auctionRepository, itemRepository, userRepository);
    }

    private Bid createBid() {
        Bid bid = new Bid();
        bid.setBidId(1);
        bid.setAuctionId(1);
        bid.setUserId(1);
        bid.setBid(10);
        return bid;
    }

    @Test
    public void shouldFailWhenAuctionNotFound() {
        Bid newBid = createBid();
        Optional<Auction> auction = Optional.empty();
        Mockito.when(auctionRepository.findById(1)).thenReturn(auction);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            bidService.add(newBid);
        });
        assertTrue(exception.getMessage().contains("Auction not found"));
    }

    @Test
    public void shouldFailWhenItemNotFound() {
        Bid newBid = createBid();
        Auction auction = new Auction();
        Optional<Item> item = Optional.empty();
        Mockito.when(auctionRepository.findById(1)).thenReturn(Optional.of(auction));
        Mockito.when(itemRepository.findById(1)).thenReturn(item);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            bidService.add(newBid);
        });
        assertTrue(exception.getMessage().contains("Item not found"));
    }

    @Test
    public void shouldFailWhenUserNotFound() {
        Bid newBid = createBid();
        Auction auction = new Auction();
        auction.setItemId(1);
        Item item = new Item();
        Optional<User> user = Optional.empty();
        Mockito.when(auctionRepository.findById(1)).thenReturn(Optional.of(auction));
        Mockito.when(itemRepository.findById(1)).thenReturn(Optional.of(item));
        Mockito.when(userRepository.findById(1)).thenReturn(user);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            bidService.add(newBid);
        });
        assertTrue(exception.getMessage().contains("User not found"));
    }

    @Test
    public void shouldFailWhenBidOwnItem() {
        Bid newBid = createBid();
        Auction auction = new Auction();
        auction.setItemId(1);
        Item item = new Item();
        item.setUserId(1);
        User user = new User();
        Mockito.when(auctionRepository.findById(1)).thenReturn(Optional.of(auction));
        Mockito.when(itemRepository.findById(1)).thenReturn(Optional.of(item));
        Mockito.when(userRepository.findById(1)).thenReturn(Optional.of(user));
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            bidService.add(newBid);
        });
        assertTrue(exception.getMessage().contains("User cannot bid on their own item"));
    }

    @Test
    public void shouldFailWhenBidNotHighest() {
        Bid newBid = createBid();
        Auction auction = new Auction();
        auction.setItemId(1);
        Item item = new Item();
        item.setUserId(2);
        User user = new User();
        Bid currentHighest = new Bid();
        currentHighest.setBid(20);
        Mockito.when(auctionRepository.findById(1)).thenReturn(Optional.of(auction));
        Mockito.when(itemRepository.findById(1)).thenReturn(Optional.of(item));
        Mockito.when(userRepository.findById(1)).thenReturn(Optional.of(user));
        Mockito.when(bidRepository.getHighestBidForAnAuction(1)).thenReturn(currentHighest);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            bidService.add(newBid);
        });
        assertTrue(exception.getMessage()
                .contains("Bid must be higher than the current highest bid of " + currentHighest.getBid() + " tokens"));
    }

    @Test
    public void shouldFailWhenBidNotHigherThanStaring() {
        Bid newBid = createBid();
        Auction auction = new Auction();
        auction.setItemId(1);
        auction.setStartingBid(15);
        Item item = new Item();
        item.setUserId(2);
        User user = new User();
        Mockito.when(auctionRepository.findById(1)).thenReturn(Optional.of(auction));
        Mockito.when(itemRepository.findById(1)).thenReturn(Optional.of(item));
        Mockito.when(userRepository.findById(1)).thenReturn(Optional.of(user));
        Mockito.when(bidRepository.getHighestBidForAnAuction(1)).thenReturn(null);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            bidService.add(newBid);
        });
        assertTrue(exception.getMessage().contains("Bid must be higher than the starting bid of " + "15" + " tokens"));
    }

    @Test
    public void shouldFailWhenSameUserNotEnoughToken() {
        Bid newBid = createBid();
        Auction auction = new Auction();
        auction.setItemId(1);
        Item item = new Item();
        item.setUserId(2);
        User newBidUser = new User();
        newBidUser.setUserId(1);
        newBidUser.setTokens(3);
        Bid currentHighest = new Bid();
        currentHighest.setBid(5);
        currentHighest.setUserId(1);
        Mockito.when(auctionRepository.findById(1)).thenReturn(Optional.of(auction));
        Mockito.when(itemRepository.findById(1)).thenReturn(Optional.of(item));
        Mockito.when(userRepository.findById(1)).thenReturn(Optional.of(newBidUser));
        Mockito.when(bidRepository.getHighestBidForAnAuction(1)).thenReturn(currentHighest);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            bidService.add(newBid);
        });
        assertTrue(exception.getMessage().contains("User does not have enough tokens"));
    }

    @Test
    public void shouldFailWhenDifferentUserNotEnoughToken() {
        Bid newBid = createBid();
        Auction auction = new Auction();
        auction.setItemId(1);
        Item item = new Item();
        item.setUserId(2);
        User newBidUser = new User();
        newBidUser.setUserId(1);
        newBidUser.setTokens(3);
        Bid currentHighest = new Bid();
        currentHighest.setBid(5);
        currentHighest.setUserId(3);
        Mockito.when(auctionRepository.findById(1)).thenReturn(Optional.of(auction));
        Mockito.when(itemRepository.findById(1)).thenReturn(Optional.of(item));
        Mockito.when(userRepository.findById(1)).thenReturn(Optional.of(newBidUser));
        Mockito.when(bidRepository.getHighestBidForAnAuction(1)).thenReturn(currentHighest);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            bidService.add(newBid);
        });
        assertTrue(exception.getMessage().contains("User does not have enough tokens"));
    }

    @Test
    public void shouldAddNewBid() {
        Bid newBid = createBid();
        Auction auction = new Auction();
        auction.setItemId(1);
        Item item = new Item();
        item.setUserId(2);
        User newBidUser = new User();
        newBidUser.setUserId(1);
        newBidUser.setTokens(50);
        Bid currentHighest = new Bid();
        currentHighest.setBid(5);
        currentHighest.setUserId(3);
        Mockito.when(auctionRepository.findById(1)).thenReturn(Optional.of(auction));
        Mockito.when(itemRepository.findById(1)).thenReturn(Optional.of(item));
        Mockito.when(userRepository.findById(1)).thenReturn(Optional.of(newBidUser));
        Mockito.when(bidRepository.getHighestBidForAnAuction(1)).thenReturn(currentHighest);
        Mockito.when(bidRepository.save(newBid)).thenReturn(newBid);
        int actual = bidService.add(newBid);
        Assertions.assertEquals(1, actual);
    }

    @Test
    public void shouldUpdateBid() {
        Bid bid = createBid();
        Mockito.when(bidRepository.findById(1)).thenReturn(Optional.of(bid));
        bidService.update(bid);
        verify(bidRepository).save(bid);
    }

    @Test
    public void shouldDeleteBid() {
        Bid bid = createBid();
        Mockito.when(bidRepository.findById(1)).thenReturn(Optional.of(bid));
        bidService.delete(bid.getBidId());
        verify(bidRepository).deleteById(bid.getBidId());
    }

    @Test
    public void shouldDeleteAllBidForAnAuction() {
        int auctionId = 1;
        bidService.deleteForAuction(auctionId);
        verify(bidRepository).deleteAllBidsForAnAuction(auctionId);
    }

    @Test
    public void shouldGetBidById() {
        Bid bid = createBid();
        Mockito.when(bidRepository.findById(1)).thenReturn(Optional.of(bid));
        Bid actual = bidService.getById(1);
        Assertions.assertEquals(1, actual.getBidId());
        Assertions.assertEquals(1, actual.getAuctionId());
    }

    @Test
    public void shouldGetBidByIdNull() {
        Optional<Bid> bid = Optional.empty();
        Mockito.when(bidRepository.findById(1)).thenReturn(bid);
        Bid actual = bidService.getById(1);
        Assertions.assertEquals(null, actual);
    }

    @Test
    public void shouldGetAllBids() {
        bidService.getAll();
        verify(bidRepository).findAll();
    }

    @Test
    public void shouldGetHighestBidForAnAuction() {
        bidService.getHighestBidForAnAuction(1);
        verify(bidRepository).getHighestBidForAnAuction(1);
    }
}