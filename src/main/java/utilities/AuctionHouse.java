package utilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.apache.commons.lang3.time.DateUtils;

import auction.Auction;
import auction.Item;
import auction.Lot;
import user.Participant;
import user.User;
import user.userDetails.Address;
import user.userDetails.Review;

public class AuctionHouse {
	private String name;
	private int sessionCookie = 0;
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
		 		
		 		File f =  new File("src/main/resources/imgDB/profilePics/" + rs.getString("img") + ".jpg");
				   String encodstring = null;
				   try {
						encodstring = encodeFileToBase64Binary(f);
				   } catch (Exception e) {
					    e.printStackTrace();
				   }
		  	    Address a1 = new Address(rs.getString("city"),rs.getString("road"),rs.getString("postalCode"),rs.getString("number"),rs.getString("country"));
				Participant p1 = new Participant(rs.getString("firstName"), rs.getString("lastName"), rs.getString("email"), rs.getString("username"), rs.getString("password"), a1, rs.getDate("birthday").toLocalDate(), rs.getString("mobileNumber"), encodstring, rs.getString("intro"));
				list.add(p1);
				System.out.println(rs.getString(1) + "\t" + rs.getString("lastName") + rs.getDate("birthday"));
		
			}
		 	
		}catch(SQLException e) {
			   System.out.println("errore: " + e.getMessage());
	     }
		finally {
			   cn.close();
		 }  
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
			loggedIn.put(sessionCookie, p.getUsername());
	        candy = sessionCookie;
	        sessionCookie +=1;
			return candy;
			
            
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		}
		finally {
			cn.close();
		}

		   
	}
	
	public int login(String email, String pwd) throws SQLException {
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
			        loggedIn.put(sessionCookie, rs.getString("username"));
			        candy = sessionCookie;
			        sessionCookie +=1;
			   }
			   
		   } catch(SQLException e) {
			   System.out.println("errore: " + e.getMessage());
		   }
		   finally {
			   cn.close();
		   }  

		return candy;
	}
		
	public ArrayList<Review> getReviews(String username) throws SQLException{		
		Connection cn = null;
		Statement st;
		ResultSet rs1, rs2;
		String sql;
		ArrayList<Review> list = new ArrayList<Review>();
		//___________connesione___________
		try {
			 
			cn =  connectDB(); //Establishing connection	
	        sql = "SELECT * FROM reviews where receiver = '"+ username +"';";
		   //____________query___________ 
	        st = cn.createStatement(); //creo sempre uno statement sulla connesione   
		 	rs1 = st.executeQuery(sql); //faccio la query su uno statement
		 	while(rs1.next() == true) {
		 		
		        sql = "SELECT img FROM participant where username = '"+ rs1.getString("sender") +"';";	
		        st = cn.createStatement(); //creo sempre uno statement sulla connesione   
			 	rs2 = st.executeQuery(sql); //faccio la query su uno statement
			 	while(rs2.next() == true) {
			 	
				 	File f =  new File("src/main/resources/imgDB/profilePics/" + rs2.getString("img") + ".jpg");
					String encodstring = null;
					try {
						encodstring = encodeFileToBase64Binary(f);
					} catch (Exception e) {
					    e.printStackTrace();
					}
					Review r1 = new Review(rs1.getString("sender"), rs1.getString("receiver"), rs1.getString("text"), encodstring);
					list.add(r1);
			 	}
			}
		 	
		}catch(SQLException e) {
			   System.out.println("errore: " + e.getMessage());
	     }
		finally {
			   cn.close();
		   } 
		
		
		return list;
		
	}
	
	public Participant getParticipant(int cookie) throws SQLException {
		Connection cn = null;
		Statement st;
		ResultSet rs;
		String sql;
		Participant p1;
		//___________connesione___________
        
		   try {
			 
			   cn =  connectDB(); //Establishing connection
		   	
	           sql = "SELECT * FROM participant where username = '" + loggedIn.get(cookie) + "';";

	        
	        //____________query___________
			   st = cn.createStatement(); //creo sempre uno statement sulla coneesione
			   
			   rs = st.executeQuery(sql); //faccio la query su uno statement
			   while(rs.next() == true) {
				   File f =  new File("src/main/resources/imgDB/profilePics/" + rs.getString("img") + ".jpg");
				   String encodstring = null;
				   try {
						encodstring = encodeFileToBase64Binary(f);
				   } catch (Exception e) {
					    e.printStackTrace();
				   }
				  p1 = new Participant(rs.getString("firstName"), rs.getString("lastName"), rs.getString("email"), rs.getString("username"), rs.getString("password"),  rs.getDate("birthday").toLocalDate(), rs.getString("mobileNumber"), encodstring, rs.getString("intro"));
				  return p1;
			   }
			   
		   } catch(SQLException e) {
			   System.out.println("errore: " + e.getMessage());
		   }
		   finally {
			   cn.close();
		   }
		return null;

		
	}
	public  ArrayList<Auction> getClosedAuctions(int cookie) throws SQLException {
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
	           sql1 = "SELECT * FROM auction where (username = '"+ loggedIn.get(cookie) +"' or bidder = '"+ loggedIn.get(cookie)+ "' )  and status != 'open';";
			   st1 = cn.createStatement(); //creo sempre uno statement sulla coneesione		   
			   rs1 = st1.executeQuery(sql1); //faccio la query su uno statement
			   
			   java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
			   String date1, date2;
			   
			   while(rs1.next() == true) { 
				  date1 =rs1.getTimestamp("startDate").toString();
				  date2 =rs1.getTimestamp("endDate").toString();		   
				  LocalDateTime sDate = java.time.LocalDateTime.parse(date1.substring(0, date1.length()-2), formatter);
				  LocalDateTime eDate = java.time.LocalDateTime.parse(date2.substring(0, date2.length()-2), formatter);
				  LocalDateTime currentDate = LocalDateTime.parse(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()), formatter);
				  a1 = new Auction(rs1.getString("name"), rs1.getString("username"), rs1.getString("bidder"), sDate.toString().substring(0, 10), eDate.toString().substring(0, 10), rs1.getDouble("currentPrice"),  rs1.getDouble("startingPrice"), rs1.getDouble("minimumRise"), rs1.getInt("auctionID"), rs1.getBoolean("timeExt"), sDate, eDate, rs1.getString("status") );
				
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
						  
					      File f =  new File("src/main/resources/imgDB/auctionsPics/" + rs3.getString("img"));
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
		   }
		   catch (Exception e) {
			   System.out.println("errore: " + e.getMessage()); 
			   return null;
		   }
		   finally {
			   cn.close();
		   }
		   
		
		
	}
		
	public ArrayList<Auction> getOpenAuctions() throws SQLException, ParseException {
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
	           sql1 = "SELECT * FROM auction where status = 'open';";
			   st1 = cn.createStatement(); //creo sempre uno statement sulla coneesione		   
			   rs1 = st1.executeQuery(sql1); //faccio la query su uno statement
			   
			   java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
			   String date1, date2;
			   
			   while(rs1.next() == true) { 
				  date1 =rs1.getTimestamp("startDate").toString();
				  date2 =rs1.getTimestamp("endDate").toString();		   
				  LocalDateTime sDate = java.time.LocalDateTime.parse(date1.substring(0, date1.length()-2), formatter);
				  LocalDateTime eDate = java.time.LocalDateTime.parse(date2.substring(0, date2.length()-2), formatter);
				  LocalDateTime currentDate = LocalDateTime.parse(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()), formatter);
				 
				  if(currentDate.isBefore(eDate) && currentDate.isAfter(sDate)) {
					  a1 = new Auction(rs1.getString("name"), rs1.getString("username"), rs1.getString("bidder"), sDate.toString().substring(0, 10), eDate.toString().substring(0, 10), rs1.getDouble("currentPrice"),  rs1.getDouble("startingPrice"), rs1.getDouble("minimumRise"), rs1.getInt("auctionID"), rs1.getBoolean("timeExt"), sDate, eDate, rs1.getString("status"));
					  
					  
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
							  
						      File f =  new File("src/main/resources/imgDB/auctionsPics/" + rs3.getString("img"));
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
				  else { 
					   sql2 = "update auction set auction.status = 'closed' where auctionID = '" + rs1.getInt("auctionID") + "'";
					   st2 = cn.createStatement(); //creo sempre uno statement sulla coneesione		   
					   st2.executeUpdate(sql2); //faccio la query su uno statement
				  }
				  
			   }
			   cn.close();
			   return auctions;
			   
		   } catch(SQLException e) {
			   System.out.println("errore: " + e.getMessage());
		   }
		   finally {
			   cn.close();
		   }
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
	        sql = "delete from participant where email= '" + email + "'";	        
			st = cn.createStatement(); //creo sempre uno statement sulla coneesione
			st.executeUpdate(sql); //faccio la query su uno statement
			System.out.println("User eliminato");
			   
		   } catch(SQLException e) {
			   System.out.println("errore: " + e.getMessage());
		   }
		   finally {
			   cn.close();
		   }

		   System.out.println("connection terminated"); 
		
	}

	// METODO CHE SALVA LA REVIEW NEL DB
	public int saveReview(int cookie, String receiverUsername, String review) throws SQLException {
		Connection cn = null;
		Statement st;
		ResultSet rs;
		String sql;
		String senderUsername = loggedIn.get(cookie);

		try {

			cn = connectDB();
			sql = "insert into reviews(receiver, sender, text) values ('" + receiverUsername + "','" + senderUsername + "','" + review + "')";

			st = cn.createStatement();

			st.execute(sql);

			System.out.println("Review submitted.");
			System.out.println("Connection terminated.");

		} catch (SQLException e) {
			System.out.println("Error while inserting review into DB");
			System.out.println(senderUsername);
			System.out.println(receiverUsername);
			System.out.println(review);
			return -1;
		} finally {
			cn.close();
		}

		return 0;
	}

	public int saveMessage(int cookie, String receiverUsername, String message) throws SQLException  {
		// TODO Auto-generated method stub
		Connection cn = null;
		Statement st;
		ResultSet rs;
		String sql;
		String senderUsername = loggedIn.get(cookie);
		LocalDateTime now = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm");
		String nowFormatted = now.format(formatter);


		ChatMessage msg = new ChatMessage(senderUsername, receiverUsername, message, nowFormatted);
	
		   try {
			 
			cn =  connectDB(); //Establishing connection
			sql = "insert into messages(sender, receiver, message, time) values ('" + msg.getSender() + "','" + msg.getReceiver() + "','" + msg.getText() + "','" + msg.getTime() + "')";
			
			st = cn.createStatement();
			
			st.execute(sql);
			System.out.println("inserted new message on the DB");
			System.out.println("connection terminated");
            
		} catch (SQLException e) {
			
			System.out.println("Error while connecting to the database for chat");
			return -1;
		}
		   finally {
			   cn.close();
		   }
		   return 0;
		
		
	}

	// METODO PER PRENDERE LA LISTA DI USERNAME CON CUI HAI SCAMBIATO MESSAGGI
	public ArrayList<Participant> getMessageList(int cookie) throws SQLException {

		Connection cn = null;
		Statement st;
		ResultSet rs;
		String sql;
		String loggedUsername = loggedIn.get(cookie);
		ArrayList<Participant> users = new ArrayList<>();

		try {

			cn = connectDB();	// Establishing connection

			sql = "select distinct sender as userlist from messages where receiver = '" + loggedUsername + "' union select distinct receiver as userlist from messages where sender = '" + loggedUsername + "'";

			st = cn.createStatement();
			rs = st.executeQuery(sql);	// Eseguo la stringa SQL

			// Scorro tutti i risultati e aggiungo i corrispondenti Participant all'array
			while (rs.next()) {
				users.add(getProfile(rs.getString("userlist")));
			}

			return users;
			

		} catch (SQLException e) {
			e.getMessage();
		} finally {
			cn.close();
		}

		return null;
	}


	public ArrayList<ChatMessage> getMessages(int cookie, String receiverUsername) throws SQLException {
		Connection cn = null;
		Statement st;
		ResultSet rs;
		String sql;
		ArrayList<ChatMessage> messages = new ArrayList<>();
		String senderUsername = loggedIn.get(cookie);

		try {

			cn = connectDB();
			sql = "select * from messages where (sender = '" + senderUsername + "' and receiver = '" + receiverUsername + "') or ( receiver = '" + senderUsername + "' and sender = '" + receiverUsername + " ')";

			st = cn.createStatement();
			rs = st.executeQuery(sql);

			// Scorro i risultati e li aggiungo ad un array di ChatMessage
			while(rs.next()) {
				messages.add(new ChatMessage(rs.getString("sender"), rs.getString("receiver"),
						rs.getString("message"), rs.getString("time")));
			}
			System.out.println("Connection terminated.");

			return messages;

		} catch (SQLException e) {
			System.out.println("Error while connecting to the database");
		} finally {
			cn.close();
		}

		return null;
	}
	
	public Auction getAuction(String auctionID) throws SQLException {
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
				  a1 = new Auction(rs1.getString("name"), rs1.getString("username"), rs1.getString("bidder"), sDate.toString().substring(0,10), eDate.toString().substring(0,10),  rs1.getDouble("currentPrice"), rs1.getDouble("startingPrice"), rs1.getDouble("minimumRise"), rs1.getInt("auctionID"), rs1.getBoolean("timeExt"), sDate, eDate, rs1.getString("status"));
				  
				  
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
						  
					      File f =  new File("src/main/resources/imgDB/auctionsPics/" + rs3.getString("img"));
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
		   finally {
			   cn.close();
		   }
		   return null;	   
	}
	

	public String placeBid(String username , String auctionID) throws Exception {
		try {
			Auction a = getAuction(auctionID);
			if (!username.equals(a.getHighestBidder()) && !username.equals(a.getOwner()))
				{
				   Connection cn = null;
				   Statement st;
				   ResultSet rs;
				   String sql;
				   cn =  connectDB(); //Establishing connection
				   
				   sql = "select timeExt from auction where auctionID = '" + auctionID + "'";
			       st = cn.createStatement(); //creo sempre uno statement sulla coneesione
				   rs = st.executeQuery(sql); //faccio la query su uno statement
				   rs.next();
				   int extension = rs.getInt("timeExt");
				   if (extension == 0)
				   {
					   double bid = a.getHighestBid() + a.getMinimumRise();
					   sql = "update auction set auction.currentPrice = '" + bid + "' , auction.bidder = '" +  username +"' where auctionID = '" + auctionID + "'";
			           st.executeUpdate(sql); 
			           return "You placed a bid";
				   }
				   else {
					   double bid = a.getHighestBid() + a.getMinimumRise();
					   LocalDateTime newDate = a.geteDate();
					   newDate = newDate.plusDays(1);
					   sql = "update auction set auction.endDate = '" + newDate + "' , auction.currentPrice = '" + bid + "' , auction.bidder = '" +  username +"' where auctionID = '" + auctionID + "'";
			           st.executeUpdate(sql); 
			           return "You placed a bid";
				   }
				}
			else if (username.equals(a.getHighestBidder())) return "You have already placed the highest bid on this auction";
			else return "You can't blace bids on your auctions";
		} catch (SQLException e) {
			System.out.println("errore: " + e.getMessage());
			return e.getMessage();
		}
	}
	
	public String getUsername(int cookie) {
		return loggedIn.get(cookie);
	}
	
	public Participant getProfile(String username) throws SQLException {
		
		Connection cn = null;
		Statement st;
		ResultSet rs;
		String sql;
		Participant p1;
		//___________connesione___________
        
		   try {
			 
			   cn =  connectDB(); //Establishing connection
		   	
	           sql = "SELECT * FROM participant where username = '" + username + "';";

	        
	        //____________query___________
			   st = cn.createStatement(); //creo  uno statement sulla coneesione
			   rs = st.executeQuery(sql); //faccio la query su uno statement
			   while(rs.next() == true) {
				   File f =  new File("src/main/resources/imgDB/profilePics/" + rs.getString("img") + ".jpg");
				   String encodstring = null;
				   try {
						encodstring = encodeFileToBase64Binary(f);
				   } catch (Exception e) {
					    e.printStackTrace();
				   }
				  p1 = new Participant(rs.getString("firstName"), rs.getString("lastName"), rs.getString("email"), rs.getString("username"), rs.getString("password"),  rs.getDate("birthday").toLocalDate(), rs.getString("mobileNumber"), encodstring, rs.getString("intro"));
				  return p1;
			   }
		   } catch(SQLException e) {
			   System.out.println("errore: " + e.getMessage());
		   }
		   finally {
			   cn.close();
		   } 
		return null;
	}
		   
	private Connection connectDB() throws SQLException {
			Connection cn = DriverManager.getConnection("jdbc:mysql://sql11.freemysqlhosting.net:3306/sql11421731", "sql11421731", "83bkPjI9Yf");//Establishing connection
			System.out.println("Connected With the database successfully");
			return cn;		
	}
	
	//Metodo per modifica intro profilo dell'utente
	public void updateIntro(int cookie, String intro) throws Exception {
		
		Connection cn = null;
		Statement st;
		String sql;
		Participant p1;
		//___________connesione___________
        
		   try {
			 
			   cn =  connectDB(); //Establishing connection
		   	
			   sql = "update participant set participant.intro = '" + intro + "' where username = '" + loggedIn.get(cookie) + "'";

	        
	        //____________query___________
			   st = cn.createStatement(); //creo  uno statement sulla coneesione
			   st.executeUpdate(sql); //faccio la query su uno statement
		   }
		   catch (Exception e) {
			   throw new Exception(e.getMessage());
		   }
		   finally {
			   cn.close();
		   }
	}
	
	//Metodo per aggiornare indirizzo
	public void updateAddress(int cookie, String country, String city, String road, String number, String cap) throws Exception {
		Connection cn = null;
		Statement st;
		String sql;
		Participant p1;
		//___________connesione___________
        
		   try {
			 
			   cn =  connectDB(); //Establishing connection
			   sql = "delete from address where username = '" + loggedIn.get(cookie) +"'";
			   st = cn.createStatement(); //creo  uno statement sulla coneesione
			   st.executeUpdate(sql); //faccio la query su uno statement
			   sql = "insert into address(username, city, road, postalCode, number, country) values ('" + loggedIn.get(cookie) + "','" + city + "','" + road + "','" 
						+ cap + "','" + number + "','" + country + "')";
			   st.executeUpdate(sql);
		   }
		   catch (Exception e) {
			   throw new Exception(e.getMessage());
		   }
		   finally {
			   cn.close();
		   }
		
	}
	
	
	
	//Metodo per l'inserimento di una nuova asta nel database
	public Auction registerAuctionToDB(String username, Auction a) throws Exception {
		Connection cn = null;
		Statement st;
		ResultSet rs;
		String sql;
		//___________connesione___________
        
		   try {
			 
			   cn =  connectDB(); //Establishing connection
		   	
			// CREAZIONE AUCTIONS
	           sql = "select max(auctionID) from auction;";
			   st = cn.createStatement(); //creo sempre uno statement sulla coneesione		   
			   rs = st.executeQuery(sql); //faccio la query su uno statement
			   rs.next();
			   int id = rs.getInt("max(auctionID)") + 1;
			   
			   Boolean ok = a.getTimeExt();
			   sql = "insert into auction(username, auctionID, name, startDate, endDate, bidder, startingPrice, minimumRise, currentPrice, timeExt, status, approved) values ('" + username +"','" + id + "','" + a.getName() + "','" + a.getsDate() + "','" 
						+ a.geteDate() + "','" + null  +   "','" + a.getStartingPrice() +  "','" + a.getMinimumRise() +  "','" + a.getStartingPrice() + "','"+ ok.compareTo(false) + "', 'open' , 'no')";
			   st.executeUpdate(sql);
			   //cerco da quale numero partire per rinominare le immagini
			   sql = "select max(img) from item;";
			   rs = st.executeQuery(sql);
			   rs.next();
			   String img = rs.getString("max(img)");
			   int imgId = Integer.parseInt(img.substring(0, img.length()-4)) + 1;
			   
			   //inserisco lotti
			   for(int i = 0; i < a.getLots().size(); i++)
			   {
				   sql = "insert into lot(username, auctionID, lotID, name, description) values ('" + username + "','" + id + "','" + i + "','" + a.getLots().get(i).getName() + "','" + a.getLots().get(i).getDescription() + "')";
				   st.executeUpdate(sql);
				   
				   //inserisco oggetti
				   for(int k = 0; k < a.getLots().get(i).getItems().size(); k++)
				   {
					   saveImg(a.getLots().get(i).getItems().get(k).getImgFile(), imgId);
					   sql = "insert into item(username, auctionID, lotID, itemID, img, name, description) values ('" + username + "','" + id + "','" + i + "','" + k + "','" + imgId + ".jpg','" + a.getLots().get(i).getItems().get(k).getName() + "','" + a.getLots().get(i).getItems().get(k).getDescription() + "')";
					   imgId++;
					   st.executeUpdate(sql);
				   }
			   }
			   
		   } catch(SQLException e) {
			   System.out.println("errore: " + e.getMessage());
			   throw new Exception("Error while connecting to DataBase, try again later");
		   } 
		   finally {
			   cn.close();
		   }
		   return null;
		   
	}
	


	public void updateCard(int cookie, String fName, String lName, String date, String number, String cvv) throws Exception {
		Connection cn = null;
		Statement st;
		String sql;
		Participant p1;
		//___________connesione___________
        
		   try {
			 
			   cn =  connectDB(); //Establishing connection
			   sql = "delete from address where username = '" + loggedIn.get(cookie) +"'";
			   st = cn.createStatement(); //creo  uno statement sulla coneesione
			   st.executeUpdate(sql); //faccio la query su uno statement
			   Random r = new Random();
			   double randomValue = 1999999999 * r.nextDouble();
			   sql = "insert into cCard(username, fName, lName, date, number, cvv, funds) values ('" + loggedIn.get(cookie) + "','" + fName + "','" + lName + "','" 
						+ date + "','" + number + "','" + cvv + "','" + randomValue + "')";
			   st.executeUpdate(sql);
		   }
		   catch (Exception e) {
			   throw new Exception(e.getMessage());
		   }
		   finally {
			   cn.close();
		   }
		
	}
	
	
	
	public void updateImg(int cookie, InputStream fileContent) throws Exception {
		Connection cn = null;
		Statement st;
		ResultSet rs;
		String sql;
		try {
			 
			cn =  connectDB(); //Establishing connection	
	        sql = "SELECT img FROM participant where username = '"+ loggedIn.get(cookie) +"';";
	        st = cn.createStatement(); //creo sempre uno statement sulla connesione   
		 	rs = st.executeQuery(sql); //faccio la query su uno statement
		 	rs.next();
		 	if(!rs.getString("img").equals("0"))
		 	{
				File file = new File("src/main/resources/imgDB/profilePics/" + rs.getString("img") + ".jpg");
				FileOutputStream outputStream = new FileOutputStream(file, false);
		        int read;
		        byte[] bytes = new byte[fileContent.available()];
		        while ((read = fileContent.read(bytes)) != -1) {
		             outputStream.write(bytes, 0, read);
		        }
		 	}
		 	else {
		 		sql = "select max(img) from participant;";	   
				rs = st.executeQuery(sql); //faccio la query su uno statement
				rs.next();
				int imgId = Integer.parseInt(rs.getString("max(img)")) + 1;
				File file = new File("src/main/resources/imgDB/profilePics/" + imgId + ".jpg");
				FileOutputStream outputStream = new FileOutputStream(file, false);
		        int read;
		        byte[] bytes = new byte[fileContent.available()];
		        while ((read = fileContent.read(bytes)) != -1) {
		             outputStream.write(bytes, 0, read);
		        }
		        sql = "update participant set participant.img = '" + imgId + "' where username = '" + loggedIn.get(cookie) + "'";
				st.executeUpdate(sql); //faccio la query su uno statement
		 	}
		} catch (SQLException e) {
			System.out.println("errore: " + e.getMessage());
			   throw new Exception("Error while connecting to DataBase, try again later");
		}
		finally {
		   cn.close();
	   }
		
	}
	
			
	private void saveImg(InputStream imgFile, int id) {
		File file = new File("src/main/resources/imgDB/auctionsPics/"+id+".jpg");
		try (FileOutputStream outputStream = new FileOutputStream(file, false)) {
            int read;
            byte[] bytes = new byte[imgFile.available()];
            while ((read = imgFile.read(bytes)) != -1) {
                outputStream.write(bytes, 0, read);
            }
		}catch (Exception e) {
				System.out.println("errore: " + e.getMessage());
			}
	}


	private static String encodeFileToBase64Binary(File file) throws Exception{
	        FileInputStream fileInputStreamReader = new FileInputStream(file);
	        byte[] bytes = new byte[(int)file.length()];
			fileInputStreamReader.read(bytes);
			byte[] encodedBytes = Base64.getEncoder().encode(bytes);
			return new String(encodedBytes);
	}


	public int paymentNextStep(int cookie, String auctionID) throws Exception {
		Auction a = getAuction(auctionID);
		if (a.getStatus().equals("closed"))
		{
			Connection cn = null;
			Statement st;
			String sql;
			ResultSet rs;
			Participant p1;
			//___________connesione___________
	        
			   try { 
				   cn =  connectDB(); //Establishing connection
				   sql = "select funds from cCard where username = '" + a.getHighestBidder() +"'";
				   st = cn.createStatement(); //creo  uno statement sulla coneesione
				   rs = st.executeQuery(sql); //faccio la query su uno statement
				   rs.next();
				   if(rs.getDouble("funds") >= a.getHighestBid()){	
					   	sql = "update cCard set funds = '"+ (rs.getDouble("funds") - a.getHighestBid()) + "' where username = '" + a.getHighestBidder()+ "'";
				   		st.executeUpdate(sql);
				   }
				   else{
					   throw new Exception("Insufficient funds on card, please recharge and try again");
				   }
				   sql = "update auctionHouse set amount = amount +" + a.getHighestBid();
			   	   st.executeUpdate(sql); 
			   	   sql = "update auction set status = 'on delivery' where auctionID ='" + a.getId() + "'";
			   	   st.executeUpdate(sql); 
			   	   return 0;
			   }
			   catch (Exception e) {
				   throw new Exception(e.getMessage());
			   }
			   finally {
				   cn.close();
			   }
		}
		else if (a.getStatus().equals("on delivery"))
		{
			Connection cn = null;
			Statement st;
			String sql;
			ResultSet rs;
			Participant p1;
			//___________connesione___________
	        
			   try { 
				   cn =  connectDB(); //Establishing connection
				   sql = "update auctionHouse set amount = amount -" + a.getHighestBid();
				   st = cn.createStatement(); 
			   	   st.executeUpdate(sql); 
			   	   sql = "update cCard set funds = funds + " + a.getHighestBid() + " where username = '" + a.getOwner()+ "'";
				   st.executeUpdate(sql);
			   	   sql = "update auction set status = 'paid' where auctionID ='" + a.getId() + "'";
			   	   st.executeUpdate(sql); 
			   	   return 0;
			   }
			   catch (Exception e) {
				   throw new Exception(e.getMessage());
			   }
			   finally {
				   cn.close();
			   }
		}
		else if (a.getStatus().equals("paid"))
		{
			return 1;
		}
		
		return -1;
	}

	public void deleteAuction(String auctionID) throws Exception {
		Connection cn = null;
		Statement st;
		ResultSet rs;
		String sql;
		 try { 
			   cn =  connectDB(); //Establishing connection
			   sql = "delete from item where auctionID = '" + auctionID + "'";
			   st = cn.createStatement(); 
		   	   st.executeUpdate(sql); 
			   sql = "delete from lot where auctionID = '" + auctionID + "'";
			   st.executeUpdate(sql);
			   sql = "delete from auction where auctionID = '" + auctionID + "'";
		   	   st.executeUpdate(sql); 
		   }
		   catch (Exception e) {
			   throw new Exception(e.getMessage());
		   }
		   finally {
			   cn.close();
		   }
		
	}
	
	

//METODI MODERATOR
	

	public void approveAuction(String auctionID) throws Exception {
		Connection cn = null;
		Statement st;
		String sql;
		ResultSet rs;
		Participant p1;
		//___________connesione___________
        
		   try { 
			   cn =  connectDB(); //Establishing connection
			   st = cn.createStatement(); 
		   	   sql = "update auction set approved = 'yes' where auctionID ='" + auctionID + "'";
		   	   st.executeUpdate(sql); 
		   	   Auction a = getAuction(auctionID);
		   	   auctionHouseSendMsg(a.getOwner(), "Auction: " + a.getName() + " approved");
		   }
		   catch (Exception e) {
			   throw new Exception(e.getMessage());
		   }
		   finally {
			   cn.close();
		   }	
	}

	public void rejectAuction(String auctionID) throws Exception {
		Connection cn = null;
		Statement st;
		String sql;
		ResultSet rs;
		Participant p1;
		//___________connesione___________
        
		   try { 
			   Auction a = getAuction(auctionID);
		   	   auctionHouseSendMsg(a.getOwner(), "Auction: " + a.getName() + " rejected");
			   cn =  connectDB(); //Establishing connection
			   st = cn.createStatement(); 
		   	   sql = "delete auction where auctionID ='" + auctionID + "'";
		   	   st.executeUpdate(sql); 
		   }
		   catch (Exception e) {
			   throw new Exception(e.getMessage());
		   }
		   finally {
			   cn.close();
		   }	
	}

	public void auctionHouseSendMsg(String receiver, String msg) throws Exception
	{
		
		Connection cn = null;
		Statement st;
		String sql;
		ResultSet rs;
		LocalDateTime now = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm");
		String nowFormatted = now.format(formatter);
		   try {
			 
			cn =  connectDB(); //Establishing connection
			sql = "insert into messages(sender, receiver, message, time) values ('AuctionHouse','" + receiver + "','" + msg + "','" + nowFormatted + "')";
			st = cn.createStatement();
			st.execute(sql);
		   }
		   catch (Exception e) {
			   throw new Exception(e.getMessage());
		   }
		   finally {
			   cn.close();
		   }
	}



	


	


	
}
