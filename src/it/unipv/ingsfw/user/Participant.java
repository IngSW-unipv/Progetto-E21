package it.unipv.ingsfw.user;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class Participant {
	
	 private String firstName;
	    private String lastName;
	    private String email;
	    private String username;
	    private int mobileNumber;
	    private String password;
	    private String address;
	    Date birthday;
	    
	    
	    public String getFirstName() {
			return firstName;
		}
		public void setFirstName(String firstName) {
			this.firstName = firstName;
		}
		public String getLastName() {
			return lastName;
		}
		public void setLastName(String lastName) {
			this.lastName = lastName;
		}
		public String getEmail() {
			return email;
		}
		public void setEmail(String email) {
			this.email = email;
		}
		public String getUsername() {
			return username;
		}
		public void setUsername(String username) {
			this.username = username;
		}
		public int getMobileNumber() {
			return mobileNumber;
		}
		public void setMobileNumber(int mobileNumber) {
			this.mobileNumber = mobileNumber;
		}
		public String getPassword() {
			return password;
		}
		public void setPasswrd(String password) {
			this.password = password;
		}
		
		public Date getBirthday() {
			return birthday;
		}
		public void setBirthday(Date birthday) {
			this.birthday = birthday;
		}
		public void setPassword(String password) {
			this.password = password;
		}
		
		public String getAddress() {
			return address;
		}
		public void setAddress(String address) {
			this.address = address;
		}
		
		public Participant(String firstName, String lastName, String email, String username, String password, String address, int mobileNumber, Date birthday) {
			
			this.firstName = firstName;
			this.lastName = lastName;
			this.email = email;
			this.username = username;
			this.mobileNumber = mobileNumber;
			this.password = password;
			this.birthday = birthday;
			
			
			
		}
	

	public Participant() {
			// TODO Auto-generated constructor stub
		}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		Participant p1 = new Participant ();
		p1.setFirstName("CRISELE");
		p1.setLastName("ARIOLA");
		p1.setUsername("crisele05");
		p1.setPassword("password");
		p1.setMobileNumber(380000000);
		p1.setAddress("via Rismondo 72");
	
		p1.setBirthday(java.sql.Date.valueOf("2013-09-04"));
		
		
		
		
		System.out.println(p1.getBirthday());

	} 


}
