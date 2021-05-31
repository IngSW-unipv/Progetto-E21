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
	    
	    public Participant(String firstName, String lastName, String email, String username, String password, String address, String mobileNumber, LocalDate birthday) {
			
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
				// Statements allow to issue SQL queries to the database
	            st = cn.createStatement();
	            // Result set get the result of the SQL query
	            rs = st.executeQuery("select * from participant");
	                  
	            writeResultSet(rs);

	            // PreparedStatements can use variables and are more efficient
	            PreparedStatement preparedStatement = cn .prepareStatement("insert into participant ( ?, ?, ?, ? , ?, ?, ?, ? )");
	               
	            // "myuser, webpage, datum, summary, COMMENTS from feedback.comments");
	            // Parameters start with 1
	            java.sql.Date sqlDate = java.sql.Date.valueOf(bDay);
	            
	            
	            preparedStatement.setString(1, firstName);
	            preparedStatement.setString(2, lastName);
	            preparedStatement.setString(3, username);
	            preparedStatement.setDate(6, sqlDate);
	            preparedStatement.setString(5, email);
	            preparedStatement.setString(4, password);
	            preparedStatement.setString(8, mobileNumber);
	            preparedStatement.setString(7, address);
	            preparedStatement.executeUpdate();
	            
	            cn.close();
				System.out.println("connection terminated"); 
	            
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
	


