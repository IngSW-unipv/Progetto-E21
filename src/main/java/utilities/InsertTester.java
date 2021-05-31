package utilities;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Date;

import user.Participant;

public class InsertTester {

	public static void main(String[] args) throws SQLException {
			
		java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy");
		java.time.LocalDate birthday = java.time.LocalDate.parse("26/03/1987", formatter);
		
			Participant p1 = new Participant ("Crisele", "Ariola", "crisele05@gmail.com", "crisele05", "Password", "via Rismondo 72", birthday, "1234567890");
			
			System.out.println(birthday);
			p1.registerParticipantToDB();
		}
	

}
