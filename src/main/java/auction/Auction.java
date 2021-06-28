package auction;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;

public class Auction {
	private String name, owner, highestBidder = "";
	private LocalDate sDate, eDate;
	private double startingPrice, minimumRise, highestBid = 0;
	private int id;
	private ArrayList<Lot> lots = new ArrayList<Lot>();
	
	public Auction(String name, String owner, String highestBidder, LocalDate sDate, LocalDate eDate, double startingPrice, double minimumRise, int id, double highestBid) {
		super();
		this.name = name;
		this.owner = owner;
		this.highestBidder = highestBidder;
		this.sDate = sDate;
		this.eDate = eDate;
		this.startingPrice = startingPrice;
		this.minimumRise = minimumRise;
		this.id = id;
		this.highestBid = highestBid;
	}
	
	public Auction(String name, String owner, LocalDate sDate, LocalDate eDate, double startingPrice, double minimumRise, int id) {
		super();
		this.name = name;
		this.owner = owner;
		this.sDate = sDate;
		this.eDate = eDate;
		this.startingPrice = startingPrice;
		this.minimumRise = minimumRise;
		this.id = id;
	}
	
	public String getThumbnail() {
		return lots.get(0).getItems().get(0).getImage();	
	}
	
	
	public double getHighestBid() {
		return highestBid;
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

	public LocalDate getsDate() {
		return sDate;
	}

	public LocalDate geteDate() {
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
