package user;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;

import user.userDetails.Address;

/**
 * This class describes a Participant and all its details
 */
public class Participant extends User{
	
	    private String email, img, intro;
	    LocalDate bDay;
	    Address address;
	    
	    public Participant(String firstName, String lastName, String email, String username, String password, LocalDate birthday, String mobileNumber) {
			super(firstName, lastName, username, mobileNumber, password);
			this.email = email;
			this.bDay = birthday;
			
		}
	    
	    public Participant(String firstName, String lastName, String email, String username, String password, LocalDate birthday, String mobileNumber, String img, String intro) {
			super(firstName, lastName, username, mobileNumber, password);
			this.email = email;
			this.bDay = birthday;
			this.img = img;
			this.intro = intro;
		}
	    
	    
	    public Participant(String firstName, String lastName, String email, String username, String password, Address address, LocalDate birthday, String mobileNumber, String img, String intro) {
			super(firstName, lastName, username, mobileNumber, password);
			this.email = email;
			this.bDay = birthday;
			this.address = address;
			this.img = img;
			this.intro = intro;
		}

	    

		public String getEmail() {
			return email;
		}
		
		public LocalDate getBirthday() {
			return bDay;
		}
		
		public Address getAddress() {
			return address;
		}
		
		
		public String getImg() {
			return img;
		}

		public String getIntro() {
			return intro;
		}

		public LocalDate getbDay() {
			return bDay;
		}
		
			   
}
	


