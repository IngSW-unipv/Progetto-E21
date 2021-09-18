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
import user.userDetails.CreditCard;
import user.userDetails.Review;

/**
 * This class describes the Auction House and all the logic behind it
 */
public class AuctionHouse {
	private String name;
	private int sessionCookie = 0, moderatorCookie = 0;
	private ArrayList<User> users, onlineUsers;
	Map<Integer, String> loggedIn = new HashMap<>();
	Map<Integer, String> moderatorLoggedIn = new HashMap<>();
	
	public String getName() {
		return name;
	}

	public AuctionHouse(String name) {
		this.name = name;
	}

	/**
	 * This method gets all the Participants registered in the DB
	 * and returns them as an ArrayList of Participants
	 *
	 * @return List of all the Participants registered to DB
	 * @throws SQLException
	 */
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

	/**
	 * This method registers the Participant given as parameter to DB
	 * @param p The Participant you want to register to DB
	 * @return Returns current session cookie
	 * @throws Exception SQL Exception
	 */
	public synchronized int registerParticipantToDB(Participant p) throws Exception {
		
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

	/**
	 * This method logins into the website using the given email and password
	 * @param email Email of the user
	 * @param pwd Password of the user
	 * @return Returns cookie of the current session
	 * @throws SQLException
	 */
	public int login(String email, String pwd) throws SQLException {
		Connection cn = null;
		Statement st;
		ResultSet rs;
		String sql;
		 int candy = -1;
		//___________connesione___________
		   try {
			 
			cn =  connectDB(); //Establishing connection
	        sql = "SELECT username FROM participant where email = '" + email + "' and password = '" + pwd + "';";
	       
		   //____________query_________
			   st = cn.createStatement(); //creo sempre uno statement sulla coneesione
			   
			   rs = st.executeQuery(sql); //faccio la query su uno statement
			   if(rs.next() == true) {
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

	/**
	 * This method checks if the user currently has a credit card registered to the website
	 * @param cookie Current session cookie
	 * @return True if the user has a credit card, false otherwise
	 */
	public boolean checkCard(int cookie) {
		CreditCard c = null;
		try {
			c = new CreditCard(loggedIn.get(cookie));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (c.getNumber()==null) return false;
		else return true;
		
	}

	/**
	 * This method outputs the reviews of the given user
	 * @param username Username of the current user
	 * @return Return an ArrayList of Reviews
	 * @throws SQLException
	 */
	public ArrayList<Review> getReviews(String username) throws SQLException{		
		Connection cn = null;
		Statement st;
		ResultSet rs1, rs2;
		String sql;
		ArrayList<Review> list = new ArrayList<Review>();
		try {
			 
			cn =  connectDB(); //Establishing connection	
	        sql = "SELECT * FROM reviews where receiver = '"+ username +"';";
		   //____________query___________ 
	        st = cn.createStatement();   
		 	rs1 = st.executeQuery(sql);
		 	while(rs1.next() == true) {
		 		
		        sql = "SELECT img FROM participant where username = '"+ rs1.getString("sender") +"';";	
		        st = cn.createStatement(); 
			 	rs2 = st.executeQuery(sql); 
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

	/**
	 * This methods outputs all current closed Auctions
	 * @param cookie Current session cookie
	 * @return Returns an ArrayList of Auctions
	 * @throws SQLException
	 */
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
				  a1 = new Auction(rs1.getString("auctionID"));
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

	/**
	 * This method outputs the current open Auctions
	 * @return Return an ArrayList of Auctions
	 * @throws SQLException
	 * @throws ParseException
	 */
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
	           sql1 = "SELECT * FROM auction where (status = 'open' or status = 'pending') and approved = 'yes';";
			   st1 = cn.createStatement(); //creo sempre uno statement sulla coneesione		   
			   rs1 = st1.executeQuery(sql1); //faccio la query su uno statement
			   
			   java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
			   String date1, date2;
			   LocalDateTime currentDate = LocalDateTime.parse(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()), formatter);
			   
			   while(rs1.next() == true) { 
				  date1 =rs1.getTimestamp("startDate").toString();
				  date2 =rs1.getTimestamp("endDate").toString();		   
				  LocalDateTime sDate = java.time.LocalDateTime.parse(date1.substring(0, date1.length()-2), formatter);
				  LocalDateTime eDate = java.time.LocalDateTime.parse(date2.substring(0, date2.length()-2), formatter);
				 
				  if(currentDate.isBefore(eDate) && currentDate.isAfter(sDate)) { //Controllo se l'asta è scaduta
					  if (rs1.getString("status").equals("pending")) //Se la data corrente è olte la data d'inizio dell'asta le imposto come aperte
					  {
						  sql2 = "update auction set auction.status = 'open' where auctionID = '" + rs1.getInt("auctionID") + "'";
						   st2 = cn.createStatement(); 
						   st2.executeUpdate(sql2);  
					  }
					  a1 = new Auction(rs1.getString("auctionID"));
					  auctions.add(a1);
				   }
				  else { //Se l'asta è scaduta la imposto come chiusaF
					   sql2 = "update auction set auction.status = 'closed' where auctionID = '" + rs1.getInt("auctionID") + "'";
					   st2 = cn.createStatement(); 
					   st2.executeUpdate(sql2); 
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

	/**
	 * This method removes a Participant from the DB
	 * @param p1 Participant you want to remove
	 * @throws SQLException
	 */
	//Metodo per eliminare un participant (NON ANCORA UTILIZZATO)
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
			System.out.println("User deleted");
			   
		   } catch(SQLException e) {
			   System.out.println("errore: " + e.getMessage());
		   }
		   finally {
			   cn.close();
		   }

		   System.out.println("connection terminated"); 
		
	}

	/**
	 * This method saves a given Review into DB
	 * @param cookie Current session cookie
	 * @param receiverUsername The username you are writing a Review for
	 * @param review Text of the Review
	 * @param auctionID ID of the Auction you are reviewing
	 * @return Returns 0 if the method worked accordingly
	 * @throws SQLException
	 */
	public int saveReview(int cookie, String receiverUsername, String review, String auctionID) throws SQLException {
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
			sql = "update auction set status = 'completed' where auctionID ='" + auctionID + "'";
		   	st.executeUpdate(sql); 

			System.out.println("Review submitted.");


		} catch (SQLException e) {
			e.printStackTrace();
			return -1;
		} finally {
			cn.close();
		}

		return 0;
	}

	/**
	 * This method outputs the Participants you had a conversation with
	 * @param cookie Current session cookie
	 * @return Returns an ArrayList of Participants
	 * @throws SQLException
	 */
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
				users.add(new Participant(rs.getString("userlist")));
			}

			return users;
			

		} catch (SQLException e) {
			e.getMessage();
		} finally {
			cn.close();
		}

		return null;
	}

	/**
	 * This method outputs all the messages exchanged between two Participants
	 * @param cookie Current session cookie
	 * @param receiverUsername Username of the receiver
	 * @return Returns an ArrayList of Chat Messages
	 * @throws SQLException
	 */
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

	/**
	 * This method places a bid increasing the total price of the Auction
	 * @param username Username of the Participant that placed the bid
	 * @param auctionID ID of the Auction
	 * @return Returns a String message that tells the user if he placed a bid or he failed
	 * @throws Exception
	 */
	public synchronized String placeBid(String username , String auctionID) throws Exception {
		try {
			Auction a = new Auction(auctionID);
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
					   java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
					   double bid = a.getHighestBid() + a.getMinimumRise();
					   LocalDateTime currentDate = LocalDateTime.parse(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()), formatter);
					   LocalDateTime endDate = a.geteDate();
					   if(currentDate.getYear() == endDate.getYear() && currentDate.getDayOfYear() == endDate.getDayOfYear()) {
						   currentDate = currentDate.plusDays(1);
					   }
					   sql = "update auction set auction.endDate = '" + currentDate + "' , auction.currentPrice = '" + bid + "' , auction.bidder = '" +  username +"' where auctionID = '" + auctionID + "'";
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

	/**
	 * Outputs the username logged in the current session
	 * @param cookie Cookie of the current session
	 * @return A String of the username
	 */
	public String getUsername(int cookie) {
		return loggedIn.get(cookie);
	}

	/**
	 * This method connects to DB
	 * @return Returns the Connection
	 * @throws SQLException
	 */
	private Connection connectDB() throws SQLException {
			Connection cn = DriverManager.getConnection("jdbc:mysql://sql11.freemysqlhosting.net:3306/sql11421731", "sql11421731", "83bkPjI9Yf");//Establishing connection
			System.out.println("Connected With the database successfully");
			return cn;		
	}

	/**
	 * This method updates the current user intro
	 * @param cookie Current session cookie
	 * @param intro String text of the intro
	 * @throws Exception
	 */
	public  void updateIntro(int cookie, String intro) throws Exception {
		
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

	/**
	 * This method updates the address
	 * @param cookie Current session cookie
	 * @param country Country you want to update
	 * @param city City you want to update
	 * @param road Street you want to update
	 * @param number Number you want to update
	 * @param cap Postal code you want to update
	 * @throws Exception
	 */
	public  void updateAddress(int cookie, String country, String city, String road, String number, String cap) throws Exception {
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


	/**
	 * This method inserts a new Auction into DB
	 * @param username Username that created the new Auction
	 * @param a The Auction an user just created
	 * @return Returns auctionID if successful or an error message otherwise
	 * @throws Exception
	 */
	public  String registerAuctionToDB(String username, Auction a) throws Exception {
		Connection cn = null;
		Statement st;
		ResultSet rs;
		String sql;
		String id = null;
		//___________connesione___________
        
		   try {
			 
			   cn =  connectDB(); //Establishing connection
		   	
			// CREAZIONE AUCTIONS
	           sql = "select max(auctionID) from auction;";
			   st = cn.createStatement(); //creo sempre uno statement sulla coneesione		   
			   rs = st.executeQuery(sql); //faccio la query su uno statement
			   rs.next();
			   id = String.valueOf(rs.getInt("max(auctionID)") + 1);
			   
			   Boolean ok = a.getTimeExt();
			   sql = "insert into auction(username, auctionID, name, startDate, endDate, bidder, startingPrice, minimumRise, currentPrice, timeExt, status, approved) values ('" + username +"','" + id + "','" + a.getName() + "','" + a.getsDate() + "','" 
						+ a.geteDate() + "','" + null  +   "','" + a.getStartingPrice() +  "','" + a.getMinimumRise() +  "','" + a.getStartingPrice() + "','"+ ok.compareTo(false) + "', 'pending' , 'no')";
			   st.executeUpdate(sql);
			   //cerco da quale numero partire per rinominare le immagini
			   sql = "select max(img) from item;";
			   rs = st.executeQuery(sql);
			   rs.next();
			   int imgId = rs.getInt("max(img)") + 1;
			   
			   //inserisco lotti
			   for(int i = 0; i < a.getLots().size(); i++)
			   {
				   sql = "insert into lot(username, auctionID, lotID, name, description) values ('" + username + "','" + id + "','" + i + "','" + a.getLots().get(i).getName() + "','" + a.getLots().get(i).getDescription() + "')";
				   st.executeUpdate(sql);
				   
				   //inserisco oggetti
				   for(int k = 0; k < a.getLots().get(i).getItems().size(); k++)
				   {
					   saveImg(a.getLots().get(i).getItems().get(k).getImgFile(), imgId);
					   sql = "insert into item(username, auctionID, lotID, itemID, img, name, description) values ('" + username + "','" + id + "','" + i + "','" + k + "','" + imgId + "','" + a.getLots().get(i).getItems().get(k).getName() + "','" + a.getLots().get(i).getItems().get(k).getDescription() + "')";
					   imgId++;
					   st.executeUpdate(sql);
				   }
			   }
			   return id;
			   
		   } catch(SQLException e) {
			   System.out.println("errore: " + e.getMessage());
			   deleteAuction(id); //Elimino le parti eventualmente inserite nel DB
			   throw new Exception("Error while connecting to DataBase, try again later");
		   } 
		   finally {
			   cn.close();
		   }
		   
	}

	/**
	 * This method updates the user image profile
	 * @param cookie Current session cookie
	 * @param fileContent An InputStream of the image you want to update into DB
	 * @throws Exception
	 */
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

	/**
	 * This method saves the images inserted into DB
	 * @param imgFile InputStream of the img file
	 * @param id ID of the image
	 */
	//Metodo per il salvataggio delle immagini	
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

	/**
	 * This method encodes images into base64
	 * @param file Image file passed to encode
	 * @return Returns the encoded bytes of the image
	 * @throws Exception
	 */
//Metodo per la codifica in base64 delle immagini, usate nell'HTML
	private static String encodeFileToBase64Binary(File file) throws Exception{
	        FileInputStream fileInputStreamReader = new FileInputStream(file);
	        byte[] bytes = new byte[(int)file.length()];
			fileInputStreamReader.read(bytes);
			byte[] encodedBytes = Base64.getEncoder().encode(bytes);
			return new String(encodedBytes);
	}

	/**
	 * This method performs all the payment steps needed for the payment to be successful
	 * @param cookie Current session cookie
	 * @param auctionID ID of the Auction you are paying for
	 * @return Returns 0 if the method if successful
	 * @throws Exception
	 */
//Metodo che tratta i diversi step del pagamento
	public int paymentNextStep(int cookie, String auctionID) throws Exception {
		Auction a = new Auction(auctionID);
		if (a.getStatus().equals("closed")) //Primo step- Accredito denaro sul fondo di AuctionHouse
		{
			Connection cn = null;
			Statement st;
			String sql;
			ResultSet rs;
			Participant p1;
			//Controllo fondi carta
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
		else if (a.getStatus().equals("on delivery")) //Secondo step- Conferma ricezione e accredito denaro su carta venditore
		{
			Connection cn = null;
			Statement st;
			String sql;
			ResultSet rs;
			Participant p1;
			//___________connesione___________
	        
			   try { 
				   cn =  connectDB(); //Establishing connection
				   sql = "update auctionHouse set amount = amount -" + a.getHighestBid() * 0.99;
				   st = cn.createStatement(); 
			   	   st.executeUpdate(sql); 
			   	   sql = "update cCard set funds = funds + " + a.getHighestBid()* 0.99 + " where username = '" + a.getOwner()+ "'";
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
		else if (a.getStatus().equals("paid")) //terzo step- Possibilitï¿½ di scrivere recensione
		{
			return 1;
		}
		
		return -1;
	}

	/**
	 * This method removes an Auction from DB
	 * @param auctionID ID of the Auction you want to remove
	 * @throws Exception
	 */
	//Metodo per eliminare un asta
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

	/**
	 * This changes an Auction status in Approved
	 * @param auctionID ID of the Auction to approve
	 * @throws Exception
	 */
	//Metodo per approvare annuncio
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
		   	   Auction a = new Auction(auctionID);
		   	   auctionHouseSendMsg(a.getOwner(), "Auction: " + a.getName() + " approved");
		   }
		   catch (Exception e) {
			   throw new Exception(e.getMessage());
		   }
		   finally {
			   cn.close();
		   }	
	}

	/**
	 * Method that rejects an Auction
	 * @param auctionID ID of the Auction to reject
	 * @throws Exception
	 */
	//metodo per rifiutare annuncio
	public void rejectAuction(String auctionID) throws Exception {        
		   try { 
			   Auction a = new Auction(auctionID);
		   	   auctionHouseSendMsg(a.getOwner(), "Auction: " + a.getName() + " rejected");
		   	   deleteAuction(auctionID);

		   }
		   catch (Exception e) {
			   throw new Exception(e.getMessage());
		   }
	}

	/**
	 * This method sends a message from the Auction House to the user if his Auction has been approved or not
	 * @param receiver Username of the receiver
	 * @param msg String text of the message
	 * @throws Exception
	 */
	//Metodo per l'invio di messaggi da parte di auctionhouse per notificare approvazione o rifiuto dell'annuncio
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
			sql = "insert into messages(sender, receiver, message, time) values ('AuctionHouseMail','" + receiver + "','" + msg + "','" + nowFormatted + "')";
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

	/**
	 * This method logins as moderator
	 * @param username Username of the moderator
	 * @param pwd Password of the moderator
	 * @return Returns the session cookie
	 * @throws SQLException
	 */
	//Metodo login Moderatori
	public int loginModerator(String username, String pwd) throws SQLException {
		Connection cn = null;
		Statement st;
		ResultSet rs;
		String sql;
		 int candy = -1;
		//___________connesione___________
		   try {
			 
			cn =  connectDB(); //Establishing connection
	        sql = "SELECT username FROM moderator where username = '" + username + "' and password = '" + pwd + "';";
	       
		   //____________query_________
			   st = cn.createStatement(); //creo sempre uno statement sulla coneesione
			   
			   rs = st.executeQuery(sql); //faccio la query su uno statement
			   while(rs.next() == true) {
			        moderatorLoggedIn.put(moderatorCookie, rs.getString("username"));
			        candy = moderatorCookie;
			        moderatorCookie +=1;
			   }
			   
		   } catch(SQLException e) {
			   System.out.println("errore: " + e.getMessage());
		   }
		   finally {
			   cn.close();
		   }  

		return candy;
	}

	/**
	 * This method outputs a list of Auctions to be approved
	 * @return Returns an ArrayList of Auctions
	 * @throws SQLException
	 */
	//Metodo per ottenere una lista delle aste da approvare 
	public ArrayList<Auction> getPendingAuctions() throws SQLException {
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
	           sql1 = "SELECT * FROM auction where approved = 'no';";
			   st1 = cn.createStatement(); //creo sempre uno statement sulla coneesione		   
			   rs1 = st1.executeQuery(sql1); //faccio la query su uno statement
			   
			   java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
			   String date1, date2;
			   
			   while(rs1.next() == true) { 
				 a1 = new Auction(rs1.getString("auctionID"));
				 auctions.add(a1);
				  
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


	
}
