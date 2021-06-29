package utilities;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import auction.Auction;
import auction.Item;
import auction.Lot;
import user.Participant;
import user.User;
import user.userDetails.Address;

public class AuctionHouse {
	private String name;
	private int cookie = 0;
	private ArrayList<User> users, onlineUsers;
	Map<Integer, String> loggedIn = new HashMap<>();
	
	public AuctionHouse(String name) {
		this.name = name;
	}
	
	
	public  ArrayList<Participant> getAllParticipant() throws SQLException {
		Connection cn = null;
		Statement st;
		ResultSet rs;
		String sql;
		ArrayList<Participant> list = new ArrayList<Participant>();
		//___________connesione___________
		try {
			 
			cn =  connectDB(); //Establishing connection	
	        sql = "SELECT * FROM participant join address;";
		   //____________query___________
	        st = cn.createStatement(); //creo sempre uno statement sulla coneesione
			   
		 	rs = st.executeQuery(sql); //faccio la query su uno statement
		 	while(rs.next() == true) {
	  	    Address a1 = new Address(rs.getString("city"),rs.getString("road"),rs.getString("postalCode"),rs.getString("number"),rs.getString("country"));
			Participant p1 = new Participant(rs.getString("firstName"), rs.getString("lastName"), rs.getString("email"), rs.getString("username"), rs.getString("password"), a1, rs.getDate("birthday").toLocalDate(), rs.getString("mobileNumber"));
			list.add(p1);
			System.out.println(rs.getNString(1) + "\t" + rs.getNString("lastName") + rs.getDate("birthday"));
		
			}
		 	
		}catch(SQLException e) {
			   System.out.println("errore: " + e.getMessage());
	     } //fine try-catch  
		return list;
	}
	
	
	
	
	public int registerParticipantToDB(Participant p) throws Exception {
		
		Connection cn = null;
		Statement st;
		ResultSet rs;
		String sql;
		int candy = -1;
		
	
		   try {
			 
			cn =  connectDB(); //Establishing connection	
			sql = "insert into participant(firstName, lastName, email, username, password, birthday, mobileNumber) values ('" + p.getFirstName() + "','" + p.getLastName() + "','" + p.getEmail() + "','" 
					+ p.getUsername() + "','" + p.getPassword() + "','" + p.getBirthday() + "','" + p.getMobileNumber() + "')";

			st = cn.createStatement();				
			st.execute(sql);
			System.out.println("inserted new participant on the DB");
			System.out.println("connection terminated");
			cn.close();
			loggedIn.put(cookie, p.getUsername());
	        candy = cookie;
	        cookie +=1;
			return candy;
			
            
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		}

		   
	}
	
	public int login(String email, String pwd) {
		Connection cn = null;
		Statement st;
		ResultSet rs;
		String sql;
		 int candy = -1;
		//___________connesione___________
		   try {
			 
			cn =  connectDB(); //Establishing connection
	        sql = "SELECT participant.username FROM participant where email = '" + email + "' and password = '" + pwd + "';";
	       
		   //____________query_________
			   st = cn.createStatement(); //creo sempre uno statement sulla coneesione
			   
			   rs = st.executeQuery(sql); //faccio la query su uno statement
			   while(rs.next() == true) {
			        loggedIn.put(cookie, rs.getString("username"));
			        candy = cookie;
			        cookie +=1;
			   }
			   
		   } catch(SQLException e) {
			   System.out.println("errore: " + e.getMessage());
		   } //fine try-catch  

		return candy;
	}
	
	
	public Participant getParticipant(int cookie2) {
		Connection cn = null;
		Statement st;
		ResultSet rs;
		String sql;
		Participant p1;
		//___________connesione___________
        
		   try {
			 
			   cn =  connectDB(); //Establishing connection
		   	
	           sql = "SELECT * FROM participant where username = '" + loggedIn.get(cookie2) + "';";

	        
	        //____________query___________
			   st = cn.createStatement(); //creo sempre uno statement sulla coneesione
			   
			   rs = st.executeQuery(sql); //faccio la query su uno statement
			   while(rs.next() == true) {

				  p1 = new Participant(rs.getString("firstName"), rs.getString("lastName"), rs.getString("email"), rs.getString("username"), rs.getString("password"),  rs.getDate("birthday").toLocalDate(), rs.getString("mobileNumber"));
				  return p1;
			   }
			   
		   } catch(SQLException e) {
			   System.out.println("errore: " + e.getMessage());
		   } //fine try-catch  
		return null;

		
	}
	
	
	public ArrayList<Auction> getAuctions() {
		Connection cn = null;
		Statement st1, st2, st3;
		ResultSet rs1, rs2, rs3;
		String sql1, sql2, sql3;
		ArrayList<Auction> auctions = new ArrayList<Auction>();
		Auction a1;
		Lot l1;
		Item i1;
		//___________connesione___________
        
		   try {
			 
			   cn =  connectDB(); //Establishing connection
		   	
			// CREAZIONE AUCTIONS
	           sql1 = "SELECT * FROM auction;";
			   st1 = cn.createStatement(); //creo sempre uno statement sulla coneesione		   
			   rs1 = st1.executeQuery(sql1); //faccio la query su uno statement
			   
			   
			   while(rs1.next() == true) { 
				  a1 = new Auction(rs1.getString("name"), rs1.getString("username"), rs1.getString("bidder"), rs1.getDate("startDate").toLocalDate(), rs1.getDate("endDate").toLocalDate(),  rs1.getDouble("startingPrice"), rs1.getDouble("minimumRise"), rs1.getInt("auctionID"), rs1.getDouble("highestBid"));
				  
				  
				  //CREAZIONE LOT
				  sql2 = "SELECT * FROM lot where username = '" + rs1.getString("username") + "' and auctionID = " + rs1.getInt("auctionID") + ";";
				  st2 = cn.createStatement();
				  rs2 = st2.executeQuery(sql2);
				  
				  
				  while(rs2.next() == true) {
					  l1 = new Lot(rs2.getString("name"), rs2.getString("description"), rs2.getInt("lotID"));
					  
					  //CREAZIONE ITEM
					  sql3 = "SELECT * FROM item where username = '" + rs2.getString("username") + "' and auctionID = " + rs2.getInt("auctionID") + " and lotID = " + rs2.getInt("lotID") + ";";
					  st3 = cn.createStatement();
					  rs3 = st3.executeQuery(sql3);
					  
					  
					  while(rs3.next() == true) {
						  
					      File f =  new File("src/main/resources/imgDB/" + rs3.getString("img"));
					      String encodstring = null;
						  try {
								encodstring = encodeFileToBase64Binary(f);
						  } catch (Exception e) {
						    	e.printStackTrace();
						  }
						  i1 = new Item(rs3.getString("name"), rs3.getString("description"), encodstring , rs3.getInt("itemID"));
						  l1.addItem(i1);
					  }
					  a1.addLot(l1);
				  }
				  auctions.add(a1);
				  
			   }
			   return auctions;
			   
		   } catch(SQLException e) {
			   System.out.println("errore: " + e.getMessage());
		   } //fine try-catch  
		return null;

		
	}
	
	
	
	
	
	
	
	public void deleteParticipant(Participant p1) throws SQLException {
		
		
		Connection cn = null;
		Statement st;
		ResultSet rs;
		String sql;
		
		//___________connesione___________
		   try {
			 
			cn =  connectDB(); //Establishing connection	
		    String email = p1.getEmail();
	        sql = "DELETE FROM PARTICIPANT WHERE EMAIL= " + email;
		   //____________query___________
	        
			   st = cn.createStatement(); //creo sempre uno statement sulla coneesione
			   
			   rs = st.executeQuery(sql); //faccio la query su uno statement
			   while(rs.next() == true) {
				   
				   System.out.println(rs.getNString(1) + "\t" + rs.getNString("email") );
		
			   }
			   
		   } catch(SQLException e) {
			   System.out.println("errore: " + e.getMessage());
		   } //fine try-catch  
		   
		   
		   //ha dei metodi per leggere/scrivere e modificare il db registration_system
		   
		   
		   cn.close();
		   System.out.println("connection terminated"); 
		
	}


	public int saveMessage(int cookie2, String receiverUsername, String message)  {
		// TODO Auto-generated method stub
		Connection cn = null;
		Statement st;
		ResultSet rs;
		String sql;
		String senderUsername = loggedIn.get(cookie2);
	
		   try {
			 
			cn =  connectDB(); //Establishing connection
			sql = "insert into messages(sender, receiver, message, time) values ('" + senderUsername + "','" + receiverUsername + "','" + message + "','" 
					+ LocalDateTime.now().getHour() + ":" + LocalDateTime.now().getMinute() + "')";
			
			st = cn.createStatement();
			
			st.execute(sql);
			System.out.println("inserted new message on the DB");
			System.out.println("connection terminated");
			cn.close();
            
		} catch (SQLException e) {
			
			System.out.println("Error while connecting to the database");
			return -1;
		}
		   return 0;
		
		
	}


	public String[] getMessages(int cookie2, String receiverUsername) throws Exception {
		// TODO Auto-generated method stub
		Connection cn = null;
		Statement st;
		ResultSet rs;
		String sql;
		String [] messages; 
		String senderUsername = loggedIn.get(cookie2);
	
		   try {
			 
			cn =  connectDB(); //Establishing connection
			
			//prendo tutti i messaggi inviati e ricevuti da una coppia di utenti
			sql = "select count* from messages where (sender = " + senderUsername + " and receiver = " + receiverUsername + ") or ( receiver = " + senderUsername + " and sender =" + receiverUsername + ");" ;
			
			st = cn.createStatement();
			rs = st.executeQuery(sql);
			
			//conto i messaggi
			int rowcount = 0;
			System.out.println("got messages");
			if (rs.last()) {
				  rowcount = rs.getRow();
				  rs.beforeFirst();
			}
			//creo array della dimensione dei messaggi + 2 (per identificare sender e receiver)
			rowcount = (rowcount *3) + 2 ;
			messages = new String[rowcount];
			messages[0]= senderUsername;
			messages[1]= receiverUsername;
			int count = 2;
			while(rs.next() == true) {
				  messages[count] = rs.getString("sender");
				  messages[count+1] = rs.getString("message");
				  messages[count+1] = rs.getString("time");
			   }
			
			System.out.println("connection terminated");
			cn.close();
            
		} catch (SQLException e) {
			
			System.out.println("Error while connecting to the database");
			throw new Exception("Errore nel caricamento dei messaggi, riprova più tardi");
		}
		
		return messages;
	}
	
	public Auction getAuction(String auctioner, String auctionID) {
		Connection cn = null;
		Statement st1, st2, st3;
		ResultSet rs1, rs2, rs3;
		String sql1, sql2, sql3;
		Auction a1 = null;
		Lot l1;
		Item i1;
		//___________connesione___________
        
		   try {
			 
			   cn =  connectDB(); //Establishing connection
		   	
			// CREAZIONE AUCTIONS
	           sql1 = "SELECT * FROM auction where username ='" + auctioner +"' and auctionID =" + auctionID +";";
			   st1 = cn.createStatement(); //creo sempre uno statement sulla coneesione		   
			   rs1 = st1.executeQuery(sql1); //faccio la query su uno statement
			   
			   
			   while(rs1.next() == true) { 
				  a1 = new Auction(rs1.getString("name"), rs1.getString("username"), rs1.getString("bidder"), rs1.getDate("startDate").toLocalDate(), rs1.getDate("endDate").toLocalDate(),  rs1.getDouble("startingPrice"), rs1.getDouble("minimumRise"), rs1.getInt("auctionID"), rs1.getDouble("highestBid"));
				  
				  
				  //CREAZIONE LOT
				  sql2 = "SELECT * FROM lot where username = '" + rs1.getString("username") + "' and auctionID = " + rs1.getInt("auctionID") + ";";
				  st2 = cn.createStatement();
				  rs2 = st2.executeQuery(sql2);
				  
				  
				  while(rs2.next() == true) {
					  l1 = new Lot(rs2.getString("name"), rs2.getString("description"), rs2.getInt("lotID"));
					  
					  //CREAZIONE ITEM
					  sql3 = "SELECT * FROM item where username = '" + rs2.getString("username") + "' and auctionID = " + rs2.getInt("auctionID") + " and lotID = " + rs2.getInt("lotID") + ";";
					  st3 = cn.createStatement();
					  rs3 = st3.executeQuery(sql3);
					  
					  
					  while(rs3.next() == true) {
						  
					      File f =  new File("src/main/resources/imgDB/" + rs3.getString("img"));
					      String encodstring = null;
						  try {
								encodstring = encodeFileToBase64Binary(f);
						  } catch (Exception e) {
						    	e.printStackTrace();
						  }
						  i1 = new Item(rs3.getString("name"), rs3.getString("description"), encodstring , rs3.getInt("itemID"));
						  l1.addItem(i1);
					  }
					  a1.addLot(l1);
				  }
			   }
			   return a1;
			   
		   } catch(SQLException e) {
		   System.out.println("errore: " + e.getMessage());
	   } 
		   return null;
		   
	}	
		   
	private Connection connectDB() throws SQLException {
			Connection cn = DriverManager.getConnection("jdbc:mysql://sql11.freemysqlhosting.net:3306/sql11421731", "sql11421731", "83bkPjI9Yf");//Establishing connection
			System.out.println("Connected With the database successfully");
			return cn;		
	}
			
	private static String encodeFileToBase64Binary(File file) throws Exception{
	        FileInputStream fileInputStreamReader = new FileInputStream(file);
	        byte[] bytes = new byte[(int)file.length()];
			fileInputStreamReader.read(bytes);
			byte[] encodedBytes = Base64.getEncoder().encode(bytes);
			return new String(encodedBytes);
	}
	
}
