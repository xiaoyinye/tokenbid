package com.tokenbid.services;
import com.tokenbid.models.Auction;
import com.tokenbid.models.Bid;
import com.tokenbid.models.Item;
import com.tokenbid.models.User;
import com.tokenbid.repositories.AuctionRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.util.Optional;
import static org.mockito.Mockito.verify;

//don't have tests for update(), getEarliestExpiredAuction()
class AuctionServiceTest {
    private AuctionRepository auctionRepository = Mockito.mock(AuctionRepository.class);
    private EmailService emailService = Mockito.mock(EmailService.class);
    private ItemService itemService = Mockito.mock(ItemService.class);
    private BidService bidService = Mockito.mock(BidService.class);
    private UserService userService = Mockito.mock(UserService.class);
    private AuctionService auctionService;

    private Auction createAuction() {
        Auction auction = new Auction();
        auction.setAuctionId(1);
        auction.setStatus("Not Sold");
        return auction;
    }
    @BeforeEach
    public void initBefore() {
       auctionService = new AuctionService(auctionRepository, emailService, itemService, bidService, userService);
    }

    @Test
    public void shouldAddAuction() {
        Auction auction = createAuction();
        Mockito.when((auctionRepository.save(auction))).thenReturn(auction);
        int acutal = auctionService.add(auction);
        Assertions.assertEquals(auction.getAuctionId(), acutal);
    }

    @Test
    public void shouldProcessSale() {
        Item item = new Item();
        Bid bid = new Bid();
        User buyer = new User();
        User seller = new User();
        auctionService.processSale(item,bid,buyer,seller);
        verify(itemService).update(item);
        verify(bidService).deleteForAuction(bid.getAuctionId());
    }
    @Test
    @DisplayName("should delete auction by id")
    public void shouldDeleteAuctionById() {
        Auction auction = createAuction();
        Mockito.when(auctionRepository.findById(1)).thenReturn(Optional.of(auction));
        auctionService.delete(auction.getAuctionId());
        verify(auctionRepository).findById(auction.getAuctionId());
        verify(auctionRepository).deleteById(auction.getAuctionId());
    }
    @Test
    @DisplayName("should retrieve auction by id")
    public void shouldFindAuctionById() {
        Auction auction = createAuction();
        Mockito.when(auctionRepository.findById(1)).thenReturn(Optional.of(auction));
        Auction actualAuction = auctionService.getById(1);
        Assertions.assertEquals(1, actualAuction.getAuctionId());
    }

    @Test
    public void shouldFindAuctionByIdNull() {
        Optional<Auction> auction = Optional.empty();
        Mockito.when(auctionRepository.findById(1)).thenReturn(auction);
        Auction actual = auctionService.getById(1);
        Assertions.assertEquals(null, actual);
    }

    @Test
    public void shouldReturnAllAuction() {
        auctionService.getAll();
        verify(auctionRepository).findAll();
    }

    @Test
    public void shouldGetAuctionsEndingInOneHour() {
        auctionService.getAuctionsEndingInOneHour();
        verify(auctionRepository).findAuctionsEndingInNextHour();
    }

    @Test
    public void shouldNotifySold() {
        User seller = new User();
        User buyer = new User();
        Item item = new Item();
        Bid winningBid = new Bid();

    }
}