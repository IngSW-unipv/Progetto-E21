package it.unipv.ingsfw.user;
import java.sql.*;


public class ParticipantRegistration {

    private String firstName;
    private String lastName;
    private String email;
    private String username;
    private String mobileNumber;
    private String password;
	
    
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
	public String getMobileNumber() {
		return mobileNumber;
	}
	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}
	public String getPassword() {
		return password;
	}
	public void setPasswrd(String password) {
		this.password = password;
	}
	
	public ParticipantRegistration(String firstName, String lastName, String email, String username, String mobileNumber, String password) {
		
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.username = username;
		this.mobileNumber = mobileNumber;
		this.password = password;
		
		
		
	}
    
    
	public static void main(String args[]){  
	 
			 try {
					Connection	 connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/registration_system", "root", "Crisele05");//Establishing connection
					System.out.println("Connected With the database successfully");
		}
			 catch (SQLException e) {
				 System.out.println("Error while connecting to the database");
	}

	}
}
		

  