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

	/**
	 * Outputs the Lot from DB given its parameters
	 * @param name Name of the Lot
	 * @param description Description of the Lot
	 * @param lotId ID of the Lot
	 * @param auctionId ID of the Auction
	 * @param owner Owner of the auction
	 * @throws SQLException
	 */
	public Lot(String name, String description, int lotId, int auctionId, String owner) throws SQLException {
		this.name = name;
		this.description = description;
		this.lotId = lotId;
		this.owner = owner;
		this.auctionId = auctionId;
		Item i1;
		Connection cn = DriverManager.getConnection("jdbc:mysql://sql11.freemysqlhosting.net:3306/sql11421731", "sql11421731", "83bkPjI9Yf");
		Statement st;
		ResultSet rs;
		String sql;
		sql = "SELECT * FROM item where username = '" + owner + "' and auctionID = " + auctionId + " and lotID = " + lotId + ";";
		st = cn.createStatement();
		rs = st.executeQuery(sql);
		while(rs.next() == true) {
			  
		      File f =  new File("src/main/resources/imgDB/auctionsPics/" + rs.getString("img") + ".jpg");
		      String encodstring = null;
			  try {
					encodstring = encodeFileToBase64Binary(f);
			  } catch (Exception e) {
			    	e.printStackTrace();
			  }
			  i1 = new Item(rs.getString("name"), rs.getString("description"), encodstring , rs.getInt("itemID"));
			  items.add(i1);
		  }
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

	private static String encodeFileToBase64Binary(File file) throws Exception{
        FileInputStream fileInputStreamReader = new FileInputStream(file);
        byte[] bytes = new byte[(int)file.length()];
		fileInputStreamReader.read(bytes);
		byte[] encodedBytes = Base64.getEncoder().encode(bytes);
		return new String(encodedBytes);
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
