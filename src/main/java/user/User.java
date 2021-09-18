package user;

import java.io.File;
import java.io.FileInputStream;
import java.util.Base64;

public abstract class User {
	private String firstName, lastName, username, mobileNumber, password;
	
	
	
	public User(String firstName, String lastName, String username, String mobileNumber, String password) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.username = username;
		this.mobileNumber = mobileNumber;
		this.password = password;
	}



	public String getFirstName() {
		return firstName;
	}



	public String getLastName() {
		return lastName;
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

	
	
	
}
