package utilities;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import user.Participant;
import user.User;

public class AuctionHouse {
	private String name;
	private ArrayList<User> users, onlineUsers;
	
	public AuctionHouse(String name) {
		this.name = name;
	}
	
	
	public  ArrayList<Participant> getAllParticipant() throws SQLException {
		Connection cn = null;
		Statement st;
		ResultSet rs;
		String sql;
		//___________connesione___________
		   try {
			 
			cn = DriverManager.getConnection("jdbc:mysql://localhost:3306/registration_system", "root", "Crisele05");//Establishing connection
			System.out.println("Connected With the database successfully");	
		} catch (SQLException e) {
			
			System.out.println("Error while connecting to the database");
		
				}
	
		   ArrayList<Participant> list = new ArrayList<Participant>();
	        sql = "SELECT * FROM participant;";
		   //____________query___________
		   try {
			   st = cn.createStatement(); //creo sempre uno statement sulla coneesione
			   
			   rs = st.executeQuery(sql); //faccio la query su uno statement
			   while(rs.next() == true) {
				   
				   Participant p1 = new Participant(rs.getString("firstName"), rs.getString("lastName"), rs.getString("email"), rs.getString("username"), rs.getString("password"), rs.getString("address"), rs.getDate("birthday").toLocalDate(), rs.getString("mobile_number"));
				   
				   System.out.println(rs.getNString(1) + "\t" + rs.getNString("lastName") + rs.getDate("birthday"));
		
			   }
			   
		   } catch(SQLException e) {
			   System.out.println("errore: " + e.getMessage());
		   } //fine try-catch  
		return list;
	}
	
	
	
	
	public void registerParticipantToDB(Participant p) throws SQLException {
		
		Connection cn = null;
		Statement st;
		ResultSet rs;
		String sql;
		
	
		   try {
			 
			cn = DriverManager.getConnection("jdbc:mysql://localhost:3306/registration_system", "root", "Crisele05");//Establishing connection
			System.out.println("Connected With the database successfully");	
			
	
			sql = "insert into participant(firstName, lastName, email, username, password, address, birthday, mobile_number) values ('" + p.getFirstName() + "','" + p.getLastName() + "','" + p.getEmail() + "','" 
					+ p.getUsername() + "','" + p.getPassword() + "','" + p.getAddress() + "','" + p.getBirthday() + "','" + p.getMobileNumber() + "')";
			
			try {
			st = cn.createStatement();
			
			st.execute(sql);
			System.out.println("inserted new participant on the DB");
			} catch(SQLException e){
				System.out.println("email already exists");
				
			}
	
			System.out.println("connection terminated");
			cn.close();
            
		} catch (SQLException e) {
			
			System.out.println("Error while connecting to the database");
			
		}
	}
	
	public void deleteParticipant(Participant p1) throws SQLException {
		
		
		Connection cn = null;
		Statement st;
		ResultSet rs;
		String sql;
		
		//___________connesione___________
		   try {
			 
			cn = DriverManager.getConnection("jdbc:mysql://localhost:3306/registration_system", "root", "Crisele05");//Establishing connection
			System.out.println("Connected With the database successfully");	
		} catch (SQLException e) {
			
			System.out.println("Error while connecting to the database");
		
				}
		   String email = p1.getEmail();
	
	
	        sql = "DELETE FROM PARTICIPANT WHERE EMAIL= " + email;
		   //____________query___________
		   try {
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
	
}
