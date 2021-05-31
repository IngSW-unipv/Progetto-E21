package user.userDetails;

public class Address {
	

	private String city;
	private String road;
	private String postalCode;
	
	public Address(String city, String road, String postalCode) {
		this.city = city;
		this.road = road;
		this.postalCode = postalCode;
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
