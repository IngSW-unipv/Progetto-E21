package auction;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Base64;

/**
 * This class describes an Auction Lot
 */
public class Lot {
	
	private String name, description, owner;
	private int lotId, auctionId;
	private ArrayList<Item> items = new ArrayList<Item>();
	

	public Lot(String name, String description, int lotId) {
		this.name = name;
		this.description = description;
		this.lotId = lotId;
	}
	
	public int getLotId() {
		return lotId;
	}



	public Lot(String name) {
		this.name = name;
	}


	public String getName() {
		return name;
	}

	
	public String getDescription() {
		return description;
	}

	public ArrayList<Item> getItems() {
		return items;
	}

	public void addItem(Item it) {
		items.add(it);
	}
	
	
	
	
}
