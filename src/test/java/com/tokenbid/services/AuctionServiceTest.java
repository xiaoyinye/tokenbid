package com.tokenbid.services;
import com.tokenbid.models.Auction;
import com.tokenbid.models.Bid;
import com.tokenbid.models.Item;
import com.tokenbid.models.User;
import com.tokenbid.repositories.AuctionRepository;
import com.tokenbid.utils.EmailUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.util.Optional;
import static org.mockito.Mockito.verify;

//don't have tests for update()
class AuctionServiceTest {
    private AuctionRepository auctionRepository = Mockito.mock(AuctionRepository.class);
    private EmailUtil emailUtil = Mockito.mock(EmailUtil.class);
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
       auctionService = new AuctionService(auctionRepository, emailUtil, itemService, bidService, userService);
    }

    @Test
    public void shouldAddAuction() {
        Auction auction = createAuction();
        Mockito.when((auctionRepository.save(auction))).thenReturn(auction);
        int acutal = auctionService.add(auction);
        Assertions.assertEquals(auction.getAuctionId(), acutal);
    }

    @Test
    public void shouldUpdateSold() {
        Auction auction = new Auction();
        auction.setStatus("Sold");
        auction.setAuctionId(1);
        Item item = new Item();
        item.setItemId(1);
        User seller = new User();
        seller.setUserId(1);
        Bid winningBid = new Bid();
        winningBid.setUserId(2);
        User buyer = new User();
        Mockito.when(auctionRepository.findById(1)).thenReturn(Optional.of(auction));
        Mockito.when(itemService.getById(1)).thenReturn(item);
        Mockito.when(userService.getById(1)).thenReturn(seller);
        Mockito.when(bidService.getHighestBidForAnAuction(1)).thenReturn(winningBid);
        Mockito.when(userService.getById(2)).thenReturn(buyer);
//        String soldBody = "<div style=\"background-color:#ad8; min-height: 300px\">" +
//                "<div style=\"font-family: Verdana,Arial,sans-serif; font-size: 24px; font-weight: bold; background-color: black; color: white; padding: 0.5em\">"
//                +
//                "<p style=\"margin: 0; padding: 0; text-align: center\">Item Sold</p>" +
//                "</div>" +
//                "<div style=\"font-family: Verdana,Arial,sans-serif; font-size: 16px; margin: 0; padding-left:0.5em\">"
//                +
//                "<p style=\"margin-top: 1.5em\">Hi " + seller.getFirstName() + " " + seller.getLastName() + ",</p>" +
//                "<p style=\"margin-top: 1.5em\">Congratulations! Your item '" + item.getTitle() + "' was sold for "
//                + winningBid.getBid() + " tokens to " + buyer.getFirstName() + " " + buyer.getLastName() + ".</p>" +
//                "</div>" +
//                "</div>";
//        String boughtBody = "<div style=\"background-color:#ad8; min-height: 300px\">" +
//                "<div style=\"font-family: Verdana,Arial,sans-serif; font-size: 24px; font-weight: bold; background-color: black; color: white; padding: 0.5em\">"
//                +
//                "<p style=\"margin: 0; padding: 0; text-align: center\">Auction Won</p>" +
//                "</div>" +
//                "<div style=\"font-family: Verdana,Arial,sans-serif; font-size: 16px; margin: 0; padding-left:0.5em\">"
//                +
//                "<p style=\"margin-top: 1.5em\">Hi " + buyer.getFirstName() + " " + buyer.getLastName() + ",</p>" +
//                "<p style=\"margin-top: 1.5em\">Congratulations! You won the bid for '" + item.getTitle() + "' at "
//                + winningBid.getBid() + " tokens.</p>" +
//                "</div>" +
//                "</div>";
//        verify(emailService).sendHTMLEmail(seller.getEmail(), "Auction Ended", soldBody);
//        verify(emailService).sendHTMLEmail(buyer.getEmail(), "Auction Ended", boughtBody);
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
    public void shouldGetActiveAuction() {
        auctionService.getActive();
        verify(auctionRepository).findAllActiveAuctions();
    }

    @Test
    public void shouldNotifySold() {
        User seller = new User();
        User buyer = new User();
        Item item = new Item();
        Bid winningBid = new Bid();
        auctionService.notifySold(seller,buyer,item,winningBid);
        String soldBody = "<div style=\"background-color:#ad8; min-height: 300px\">" +
                "<div style=\"font-family: Verdana,Arial,sans-serif; font-size: 24px; font-weight: bold; background-color: black; color: white; padding: 0.5em\">"
                +
                "<p style=\"margin: 0; padding: 0; text-align: center\">Item Sold</p>" +
                "</div>" +
                "<div style=\"font-family: Verdana,Arial,sans-serif; font-size: 16px; margin: 0; padding-left:0.5em\">"
                +
                "<p style=\"margin-top: 1.5em\">Hi " + seller.getFirstName() + " " + seller.getLastName() + ",</p>" +
                "<p style=\"margin-top: 1.5em\">Congratulations! Your item '" + item.getTitle() + "' was sold for "
                + winningBid.getBid() + " tokens to " + buyer.getFirstName() + " " + buyer.getLastName() + ".</p>" +
                "</div>" +
                "</div>";
        String boughtBody = "<div style=\"background-color:#ad8; min-height: 300px\">" +
                "<div style=\"font-family: Verdana,Arial,sans-serif; font-size: 24px; font-weight: bold; background-color: black; color: white; padding: 0.5em\">"
                +
                "<p style=\"margin: 0; padding: 0; text-align: center\">Auction Won</p>" +
                "</div>" +
                "<div style=\"font-family: Verdana,Arial,sans-serif; font-size: 16px; margin: 0; padding-left:0.5em\">"
                +
                "<p style=\"margin-top: 1.5em\">Hi " + buyer.getFirstName() + " " + buyer.getLastName() + ",</p>" +
                "<p style=\"margin-top: 1.5em\">Congratulations! You won the bid for '" + item.getTitle() + "' at "
                + winningBid.getBid() + " tokens.</p>" +
                "</div>" +
                "</div>";
        verify(emailUtil).sendHTMLEmail(seller.getEmail(), "Auction Ended", soldBody);
        verify(emailUtil).sendHTMLEmail(buyer.getEmail(), "Auction Ended", boughtBody);
    }

    @Test
    public void shouldNotifyNotSold() {
        User seller = new User();
        Item item = new Item();
        String notSoldBody = "<div style=\"background-color:#abc; min-height: 300px\">" +
                "<div style=\"font-family: Verdana,Arial,sans-serif; font-size: 24px; font-weight: bold; background-color: black; color: white; padding: 0.5em\">"
                +
                "<p style=\"margin: 0; padding: 0; text-align: center\">Item Not Sold</p>" +
                "</div>" +
                "<div style=\"font-family: Verdana,Arial,sans-serif; font-size: 16px; margin: 0; padding-left:0.5em\">"
                +
                "<p style=\"margin-top: 1.5em\">Hi " + seller.getFirstName() + " " + seller.getLastName() + ",</p>" +
                "<p style=\"margin-top: 1.5em\">Your auction for item '" + item.getTitle()
                + "' has ended with no successful bids.</p>" +
                "</div>" +
                "</div>";
        auctionService.notifyNotSold(seller, item);
        verify(emailUtil).sendHTMLEmail(seller.getEmail(), "Auction Ended", notSoldBody);
    }

    @Test
    public void shouldNotifyCancelled() {
        User seller = new User();
        Item item = new Item();
        String cancelledBody = "<div style=\"font-family: Verdana,Arial,sans-serif; font-size: 24px; font-weight: bold; background-color: black; color: white; padding: 0.5em\">"
                +
                "<p style=\"margin: 0; padding: 0; text-align: center\">Cancelled</p>" +
                "</div>" +
                "<div style=\"font-family: Verdana,Arial,sans-serif; font-size: 16px; margin: 0; padding-left:0.5em\">"
                +
                "<p style=\"margin-top: 1.5em\">Hi " + seller.getFirstName() + " " + seller.getLastName() + ",</p>" +
                "<p style=\"margin-top: 1.5em\">Your auction for item '" + item.getTitle()
                + "' was successful cancelled.</p>" +
                "</div>";
        auctionService.notifyCancelled(seller, item);
        verify(emailUtil).sendHTMLEmail(seller.getEmail(), "Auction Ended", cancelledBody);
    }
}