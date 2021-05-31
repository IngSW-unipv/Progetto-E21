package utilities;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import user.Participant;

public class AuctionHouse {
	
	
	public AuctionHouse() {
		
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
				   
				   Participant p1 = new Participant(rs.getString("first_name"), rs.getString("last_name"), rs.getString("email"), rs.getString("username"), rs.getString("password"), rs.getString("address"), rs.getString("mobile_number"), rs.getDate("birthday").toLocalDate());
				   
				   System.out.println(rs.getNString(1) + "\t" + rs.getNString("last_name") + rs.getDate("birthday"));
		
			   }
			   
		   } catch(SQLException e) {
			   System.out.println("errore: " + e.getMessage());
		   } //fine try-catch  
		return list;
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
		   String username = p1.getUsername();
	
	
	        sql = "DELETE FROM PARTICIPANT WHERE USERNAME= " + username;
		   //____________query___________
		   try {
			   st = cn.createStatement(); //creo sempre uno statement sulla coneesione
			   
			   rs = st.executeQuery(sql); //faccio la query su uno statement
			   while(rs.next() == true) {
				   
				  
				   
				   System.out.println(rs.getNString(1) + "\t" + rs.getNString("username") );
		
			   }
			   
		   } catch(SQLException e) {
			   System.out.println("errore: " + e.getMessage());
		   } //fine try-catch  
		   
		   
		   //ha dei metodi per leggere/scrivere e modificare il db registration_system
		   
		   
		   cn.close();
		   System.out.println("connection terminated"); 
		
	}
	
}
