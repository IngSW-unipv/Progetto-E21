package user.userDetails;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Random;

import user.Participant;

/**
 * Class that describes a Credit Card
 */
public class CreditCard {
	private String number, CVV, expirationDate, firstName, lastName, username;
	double funds;

	
	public CreditCard(String number, String CVV, String expirationDate, String firstName, String lastName, String username) {
		this.number = number;
		this.CVV = CVV;
		this.expirationDate = expirationDate;
		this.firstName = firstName;
		this.lastName = lastName;
		this.username = username;
	}
	
	public CreditCard(String number, String CVV, String expirationDate, String firstName, String lastName, String username, double funds) {
		this.number = number;
		this.CVV = CVV;
		this.expirationDate = expirationDate;
		this.firstName = firstName;
		this.lastName = lastName;
		this.username = username;
		this.funds = funds;
	}
	
	
	public double getFunds ()
	{
		return funds;
	}
	
	
	public void setFunds(double funds) {
		this.funds = funds;
	}

	public String getUsernamer() {
		return username;
	}
	
	public String getNumber() {
		return number;
	}

	public String getCVV() {
		return CVV;
	}

	public String getExpirationDate() {
		return expirationDate;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}
	
	
	
	
	
	
	

}
