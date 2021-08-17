package auction;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;

public class Auction {
	private String name, owner, highestBidder = "";
	private LocalDateTime sDate, eDate;
	String sDateStr, eDateStr;
	private double startingPrice, minimumRise, currentPrice = 0;
	private int id;
	boolean timeExt = false;
	private ArrayList<Lot> lots = new ArrayList<Lot>();
	
	public Auction(String name, String owner, String highestBidder, String sDate,String eDate, double currentPrice, double startingPrice, double minimumRise, int id , boolean timeExt) {
		super();
		this.name = name;
		this.owner = owner;
		this.highestBidder = highestBidder;
		this.sDateStr = sDate;
		this.eDateStr = eDate;
		this.startingPrice = startingPrice;
		this.minimumRise = minimumRise;
		this.id = id;
		this.currentPrice=currentPrice;
	}
	
	public Auction(String name, String owner, LocalDateTime sDate, LocalDateTime eDate, double startingPrice, double minimumRise, int id, boolean timeExt) {
		super();
		this.name = name;
		this.owner = owner;
		this.sDate = sDate;
		this.eDate = eDate;
		this.startingPrice = startingPrice;
		this.minimumRise = minimumRise;
		this.id = id;
		this.currentPrice = startingPrice;
		this.timeExt = timeExt;
	}
	
	public String getThumbnail() {
		return lots.get(0).getItems().get(0).getImage();	
	}
	
	public boolean getTimeExt() {
		return timeExt;
	}
	
	public double getHighestBid() {
		if (currentPrice > startingPrice)
		{
			return currentPrice;
		}
		else return startingPrice;
	}
	
	public ArrayList<String> getAllImg(){
		ArrayList<String> imgs = new ArrayList<String>();
		for (Lot lot : getLots()) {
			for (Item itm : lot.getItems()) {
				imgs.add(itm.getImage());
			}
		}
		
		return imgs;
		
	}

	public ArrayList<Lot> getLots() {
		return lots;
	}

	public void addLot(Lot l) {
		lots.add(l);
	}

	public String getName() {
		return name;
	}

	public String getOwner() {
		return owner;
	}

	public String getHighestBidder() {
		return highestBidder;
	}

	public LocalDateTime getsDate() {
		return sDate;
	}

	public String geteDateStr() {
		return eDateStr;
	}
	
	public LocalDateTime geteDate() {
		return eDate;
	}

	public double getStartingPrice() {
		return startingPrice;
	}

	public double getMinimumRise() {
		return minimumRise;
	}

	public int getId() {
		return id;
	}
	
	
	
	
}
