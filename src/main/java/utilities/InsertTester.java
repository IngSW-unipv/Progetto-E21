package utilities;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Date;

import user.Participant;
import user.userDetails.Address;

public class InsertTester {

	public static void main(String[] args) throws SQLException {
			
		java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy");
		java.time.LocalDate birthday = java.time.LocalDate.parse("26/03/1987", formatter);
		
			Address a1 = new Address("Milano", "Via Rismondo", "21093", "12", "Italy");
			Participant p1 = new Participant ("Crisele", "Ariola", "crisele05@gmail.com", "crisele05", "Password", a1, birthday, "1234567890");
			AuctionHouse a = new AuctionHouse("Ebay");
			
			System.out.println(birthday);
			a.registerParticipantToDB(p1);
		}
	

}
