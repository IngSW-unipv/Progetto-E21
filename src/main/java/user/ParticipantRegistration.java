package user;
import java.sql.*;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;

import javax.xml.crypto.Data;

import com.mysql.cj.util.StringUtils;




public class ParticipantRegistration {
	//aggiorna
	public static ArrayList<Participant> listOfParticipant() throws SQLException {
		
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
				   
				   Participant p1 = new Participant(rs.getString("first_name"), rs.getString("last_name"), rs.getString("email"), rs.getString("username"), rs.getString("password"), rs.getString("address"), rs.getInt("mobile_number"), rs.getDate("birthday"));
				   
				   System.out.println(rs.getNString(1) + "\t" + rs.getNString("last_name") + rs.getDate("birthday"));
		
			   }
			   
		   } catch(SQLException e) {
			   System.out.println("errore: " + e.getMessage());
		   } //fine try-catch  
		   
		   
		   //ha dei metodi per leggere/scrivere e modificare il db registration_system
		   
		   
		   cn.close();
		   System.out.println("connection terminated"); 
		return null;
		
	}
	
	
	public static void deleteParticipant(Participant p1) throws SQLException {
		
		
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
	
	
	public static void newParticipant(Participant p1) throws SQLException {
		
		Connection cn = null;
		Statement st;
		ResultSet rs;
		String sql;
		
	
		   try {
			 
			cn = DriverManager.getConnection("jdbc:mysql://localhost:3306/registration_system", "root", "Crisele05");//Establishing connection
			System.out.println("Connected With the database successfully");	
		} catch (SQLException e) {
			
			System.out.println("Error while connecting to the database");
		
				}
		   
		   
		   String first_name = p1.getFirstName();
		   String last_name = p1.getLastName();
		   String username = p1.getUsername();
		   String password = p1.getPassword();
		   String email = p1.getEmail();
		   String address = p1.getPassword();
		   int mobile_number = p1.getMobileNumber();
		   Calendar birthday = Calendar.getInstance();
		   birthday.setTime(p1.getBirthday());
		   String sqlData = birthday.get(Calendar.YEAR) + "-" + birthday.get(Calendar.MONTH) + "-" + birthday.get(Calendar.DAY_OF_MONTH);
		   
	
	       
		   
		   
		   sql = "INSERT INTO PARTICIPANT* VALUES('" + first_name + "', '" + last_name + "', '" + username + "', '" + password + "', '" + email + "', '" + address + "', '" + birthday + "', '" + mobile_number + "' )";
		   
		   System.out.println(sql);
		   //____________query___________
		   try {
			   st = cn.createStatement(); //creo sempre uno statement sulla coneesione
			   
			   rs = st.executeQuery(sql); //faccio la query su uno statement
			   while(rs.next() == true) {
				   
				  
				   System.out.println(rs.getNString(1) + "\t" + rs.getNString("last_name") + rs.getDate("birthday"));
		
			   }
			   
		   } catch(SQLException e) {
			   System.out.println("errore: " + e.getMessage());
		   } //fine try-catch  
		   
		   
		   //ha dei metodi per leggere/scrivere e modificare il db registration_system
		   
		   
		   cn.close();
		   System.out.println("connection terminated"); 
	}


	//connessione ed elenco dei record di una tabella
	public static void main(String[] args) throws SQLException {
		
		
		Connection cn = null;
		Statement st;
		ResultSet rs;
		String sql;
		
		//___________connesione___________
		   try {
			 
			cn = DriverManager.getConnection("jdbc:mysql://localhost:3306/registration_system", "root", "Crisele05");//Establishing connection
			System.out.println("Connected With the database successfully");
			
			Participant p1 = new Participant();
		
		
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
			
				   
				   System.out.println(rs.getNString(1) + "\t" + rs.getNString("last_name") + rs.getDate("birthday"));
		
			   }
			   
		   } catch(SQLException e) {
			   System.out.println("errore: " + e.getMessage());
		   } //fine try-catch  
		   
		   
		   //ha dei metodi per leggere/scrivere e modificare il db registration_system
		   
		   
		   cn.close();
		   System.out.println("connection terminated"); //chiusura della connessione
		
		   
		}
	
	
	
	
	
}

		

  