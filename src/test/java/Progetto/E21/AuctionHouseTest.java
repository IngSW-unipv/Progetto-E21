package Progetto.E21;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
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
		LocalDateTime sDate = java.time.LocalDateTime.parse(date1, formatter);
		LocalDateTime eDate = java.time.LocalDateTime.parse(date2, formatter);
		auction = new Auction("auction123", "owner", "user1", sDate.toString().substring(0, 10), eDate.toString().substring(0, 10), 100.0, 10.0, 1.0, 100, true, sDate, eDate, "open");
		auction.addLot(lot1);
		auction.addLot(lot2);
		house = new AuctionHouse("Auction House");
	}
	
	

    @Test
    public void placeBidTest() {
    	init();
    	double bid = 0;
    	try {
			auction = new Auction("2");
			house.placeBid("IronMan", "2");
			bid = auction.getHighestBid();
			house.placeBid("user", "2");
		} catch (Exception e) {
			e.printStackTrace();
		}
    	assertEquals(bid, auction.getHighestBid(), 0);
    }
    
    @Test
    public void userDeleteTest() {
    	init();
    	java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd");
		String date1 = "2000-02-12";
		String date2 = "2000-03-12";		   
		LocalDate sDate = java.time.LocalDate.parse(date1, formatter);
		LocalDate eDate = java.time.LocalDate.parse(date2, formatter);
    	Participant p1 = new Participant("test1", "test1", "a@a.com", "test1", "test", sDate, "1234567891");
    	try {
    		int users = house.getAllParticipant().size();
			house.registerParticipantToDB(p1);
			house.deleteParticipant(p1);
			assertEquals(users, house.getAllParticipant().size(), 0);
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
}
