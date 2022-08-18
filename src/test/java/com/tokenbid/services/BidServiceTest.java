package com.tokenbid.services;
import com.tokenbid.models.Bid;
import com.tokenbid.models.Item;
import com.tokenbid.repositories.AuctionRepository;
import com.tokenbid.repositories.BidRepository;
import com.tokenbid.repositories.ItemRepository;
import com.tokenbid.repositories.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.util.Optional;
import static org.mockito.Mockito.verify;

//Don't have tests for add()

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
        return bid;
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
        int auctionId = 1;
        bidService.getHighestBidForAnAuction(1);
        verify(bidRepository).getHighestBidForAnAuction(1);
    }
}