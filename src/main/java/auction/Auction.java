package auction;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Base64;

/**
 * This class describes an Auction
 */
public class Auction {
	private String name, owner, status, approved, highestBidder = "";
	private LocalDateTime sDate, eDate;
	private String sDateStr, eDateStr;
	private double startingPrice, minimumRise, currentPrice = 0;
	private int id;
	boolean timeExt = false;
	private ArrayList<Lot> lots = new ArrayList<Lot>();
	
	public Auction(String name, String owner, String highestBidder, String sDateStr,String eDateStr, double currentPrice, double startingPrice, double minimumRise, int id , boolean timeExt, LocalDateTime sDate, LocalDateTime eDate, String status) {
		super();
		this.name = name;
		this.owner = owner;
		this.status = status;
		this.highestBidder = highestBidder;
		this.sDateStr = sDateStr;
		this.eDateStr = eDateStr;
		this.eDate = eDate;
		this.sDate = sDate;
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

	/**
	 * Outputs an Auction from DB given its ID
	 * @param auctionID ID of the Auction
	 * @throws SQLException
	 */
	public Auction(String auctionID) throws SQLException {
		Connection cn = DriverManager.getConnection("jdbc:mysql://sql11.freemysqlhosting.net:3306/sql11421731", "sql11421731", "83bkPjI9Yf");
		Statement st1, st2;
		ResultSet rs1, rs2;
		String sql1, sql2;
		Lot l1;

		   try {
		   	
			// CREAZIONE AUCTIONS
	           sql1 = "SELECT * FROM auction where auctionID =" + auctionID +";";
			   st1 = cn.createStatement(); //creo sempre uno statement sulla coneesione		   
			   rs1 = st1.executeQuery(sql1); //faccio la query su uno statement
			   java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
			   String date1, date2;
			   
			   while(rs1.next() == true) { 
				  date1 = "20" + rs1.getTimestamp("startDate").toString();
				  date2 = "20" + rs1.getTimestamp("endDate").toString();		   
				  LocalDateTime sDate = java.time.LocalDateTime.parse(date1.substring(2, date1.length()-2), formatter);
				  LocalDateTime eDate = java.time.LocalDateTime.parse(date2.substring(2, date1.length()-2), formatter);
				  this.name = rs1.getString("name");
				  this.owner = rs1.getString("username");
				  this.highestBidder = rs1.getString("bidder");
				  this.sDateStr = sDate.toString().substring(0,10);
				  this.eDateStr = eDate.toString().substring(0,10);
				  this.currentPrice = rs1.getDouble("currentPrice");
				  this.startingPrice = rs1.getDouble("startingPrice");
				  this.eDate = eDate;
				  this.sDate = sDate;
				  this.status = rs1.getString("status");
				  this.approved = rs1.getString("approved");
				  this.timeExt = rs1.getBoolean("timeExt");
				  this.id = rs1.getInt("auctionID");
				  this.minimumRise = rs1.getDouble("minimumRise");
				  
				  
				  //CREAZIONE LOT
				  sql2 = "SELECT * FROM lot where username = '" + rs1.getString("username") + "' and auctionID = " + id + ";";
				  st2 = cn.createStatement();
				  rs2 = st2.executeQuery(sql2);
				  
				  
				  while(rs2.next() == true) {
					  l1 = new Lot(rs2.getString("name"), rs2.getString("description"), rs2.getInt("lotID"), id, owner);
					  lots.add(l1);
				  }
			   }
			   
		   } catch(SQLException e) {
			   System.out.println("errore: " + e.getMessage());
		   } 
		   finally {
			   cn.close();
		   }
	}
	/**
	 * This method encodes images into base64
	 * @param file Image file passed to encode
	 * @return Returns the encoded bytes of the image
	 * @throws Exception
	 */
	private static String encodeFileToBase64Binary(File file) throws Exception{
        FileInputStream fileInputStreamReader = new FileInputStream(file);
        byte[] bytes = new byte[(int)file.length()];
		fileInputStreamReader.read(bytes);
		byte[] encodedBytes = Base64.getEncoder().encode(bytes);
		return new String(encodedBytes);
	}
	
	public String getThumbnail() {
		return lots.get(0).getItems().get(0).getImage();	
	}
	
	public String getStatus() {
		return status;	
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

	/**
	 * Outputs all the images from a Lot
	 * @return Returns an ArrayList of encoded image Strings
	 */
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
