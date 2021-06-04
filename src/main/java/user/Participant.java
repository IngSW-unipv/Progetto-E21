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

import user.userDetails.Address;

public class Participant {
	
	    private String firstName, lastName, email, username, mobileNumber, password;
	    LocalDate bDay;
	    Address address;
	    
	    public Participant(String firstName, String lastName, String email, String username, String password, Address address, LocalDate birthday, String mobileNumber) {
			
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
		
		public Address getAddress() {
			return address;
		}
		
		
		private void writeResultSet(ResultSet rs) {
			// TODO Auto-generated method stub
			
		}
		
			   
}
	


