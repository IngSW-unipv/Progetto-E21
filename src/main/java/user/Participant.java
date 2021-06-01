package user;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;

public class Participant {
	
	    private String firstName, lastName, email, username, mobileNumber, password, address;
	    LocalDate bDay;
	    
	    public Participant(String firstName, String lastName, String email, String username, String password, String address, LocalDate birthday, String mobileNumber) {
			
			this.firstName = firstName;
			this.lastName = lastName;
			this.email = email;
			this.username = username;
			this.mobileNumber = mobileNumber;
			this.password = password;
			this.bDay = birthday;
			this.address = address;
			
		}
	    
	    
	    public String getFirstName() {
			return firstName;
		}

		public String getLastName() {
			return lastName;
		}

		public String getEmail() {
			return email;
		}

		public String getUsername() {
			return username;
		}

		public String getMobileNumber() {
			return mobileNumber;
		}

		public String getPassword() {
			return password;
		}
		
		public LocalDate getBirthday() {
			return bDay;
		}
		
		public String getAddress() {
			return address;
		}

		
		
		
		public Participant() {
			// TODO Auto-generated constructor stub
		}
		//aggiorna
		
		
		
		public void registerParticipantToDB() throws SQLException {
			
			Connection cn = null;
			Statement st;
			ResultSet rs;
			String sql;
			
		
			   try {
				 
				cn = DriverManager.getConnection("jdbc:mysql://localhost:3306/registration_system", "root", "Crisele05");//Establishing connection
				System.out.println("Connected With the database successfully");	
				
		
				sql = "insert into participant(firstName, lastName, email, username, password, address, birthday, mobile_number) values ('" + firstName + "','" + lastName + "','" + email + "','" 
						+ username + "','" + password + "','" + address + "','" + bDay + "','" + mobileNumber + "')";
				
				
				st = cn.createStatement();
				
				System.out.println("inserted new participant on the DB");
				st.execute(sql);
		
				System.out.println("connection terminated");
				cn.close();
	            
			} catch (SQLException e) {
				
				System.out.println("Error while connecting to the database");
				
			}
			    //fine try-catch  
			   
			   
			   //ha dei metodi per leggere/scrivere e modificare il db registration_system
			   
			   
			   
		}
		
		
		
		private void writeResultSet(ResultSet rs) {
			// TODO Auto-generated method stub
			
		}
		
			   
}
	


