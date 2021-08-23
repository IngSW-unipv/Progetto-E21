package Progetto.E21;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;

import auction.*;
import utilities.*;
import user.*;

/**
 * Class for JUnit test on the AuctionHouse class
 * @see AuctionHouse
 * @see Auction
 * @see Item
 * @see Lot
 * @see Team
 */
public class AuctionHouseTest {
	private static AuctionHouse house;
	private static Auction auction;
	
	private static void init() {
		Item item1 = new Item("item1", "1st item", "item1.png", 0);
		Item item2 = new Item("item2", "2nd item", "item2.png", 1);
		Item item3 = new Item("item3", "3rd item", "item3.png", 2);
		Item item4 = new Item("item4", "4th item", "item4.png", 3);
		Lot lot1 = new Lot("lot1", "1st lot", 0);
		Lot lot2 = new Lot("lot2", "2nd lot", 1);
		lot1.addItem(item1);
		lot1.addItem(item2);
		lot2.addItem(item3);
		lot2.addItem(item4);
		java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		String date1 = "2020-02-12 10:11:00";
		String date2 = "2020-03-12 10:11:00";		   
		LocalDateTime sDate = java.time.LocalDateTime.parse(date1.substring(0, date1.length()-2), formatter);
		LocalDateTime eDate = java.time.LocalDateTime.parse(date2.substring(0, date2.length()-2), formatter);
		auction = new Auction("auction123", "owner", "user1", sDate.toString().substring(0, 10), eDate.toString().substring(0, 10), 100.0, 10.0, 1.0, 100, true, eDate, "open");
		auction.addLot(lot1);
		auction.addLot(lot2);
		house = new AuctionHouse("Auction House");
	}
	
    @Test
    public void placeBidTest() {
    	init();
    	try {
			house.registerAuctionToDB("user", auction);
			house.placeBid("IronMan", "100");
			auction = house.getAuction("100");
		} catch (Exception e) {
			e.printStackTrace();
		}
    	assertEquals(110.0, auction.getHighestBid(), 0);
    }
    
    @Test
    public void userDeleteTest() {
    	init();
    	Participant p1 = new Participant("test1", "test1", "a@a.com", "test1", "test", null, "1234567891");
    	Participant p2 = new Participant("test2", "test2", "b@a.com", "test2", "test", null, "6789562738");
    	try {
			house.registerParticipantToDB(p1);
			house.registerParticipantToDB(p2);
			house.deleteParticipant(p2);
			assertEquals(1, house.getAllParticipant().size(), 0);
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
}
