package user.userDetails;

/**
 * This class describes a home address
 */
public class Address {
	

	private String city, road, postalCode, number, country;
	
	public Address(String city, String road, String postalCode, String number, String country) {
		this.city = city;
		this.road = road;
		this.postalCode = postalCode;
		this.number = number;
		this.country = country;
	}

	public String getCity() {
		return city;
	}

	public String getRoad() {
		return road;
	}

	public String getPostalCode() {
		return postalCode;
	}
	

}
