package user.userDetails;
import java.sql.Date;

public class CreditCard {
	private String number;
	private String CVV;
	private Date expirationDate;
	private String firstName;
	private String lastName;
	
	public CreditCard(String number, String CVV, Date expirationDate, String firstName, String lastName) {
		
		this.number = number;
		this.CVV = CVV;
		this.expirationDate = expirationDate;
		this.firstName = firstName;
		this.lastName = lastName;
	}

	public String getNumber() {
		return number;
	}

	public String getCVV() {
		return CVV;
	}

	public Date getExpirationDate() {
		return expirationDate;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}
	
	
	
	
	
	
	

}
