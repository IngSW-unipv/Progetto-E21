package user.userDetails;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Random;

import user.Participant;

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
	
	public CreditCard(String username) throws Exception{
		Connection cn = DriverManager.getConnection("jdbc:mysql://sql11.freemysqlhosting.net:3306/sql11421731", "sql11421731", "83bkPjI9Yf");
		Statement st;
		ResultSet rs;
		String sql;
		
		 try {
			   sql = "select * from cCard where username = '" + username +"'";
			   st = cn.createStatement(); //creo  uno statement sulla coneesione
			   rs = st.executeQuery(sql); //faccio la query su uno statement
			   if (rs.next()) {
				   this.number = rs.getString("number");
				   this.username = rs.getString("username");
				   this.CVV = rs.getString("cvv");
				   this.firstName = rs.getString("lName");
				   this.lastName = rs.getString("fName");
				   this.expirationDate = rs.getString("date");
				   this.funds = rs.getDouble("funds");
			   }
		   }
		   catch (Exception e) {
			   throw new Exception(e.getMessage());
		   }
		   finally {
			   cn.close();
		   }
		
	}
	
	

	public void toDB() throws Exception {
		Connection cn = DriverManager.getConnection("jdbc:mysql://sql11.freemysqlhosting.net:3306/sql11421731", "sql11421731", "83bkPjI9Yf");
		Statement st;
		String sql;
		Participant p1;
		//___________connesione___________
        
		   try {
			   sql = "delete from cCard where username = '" + username +"'";
			   st = cn.createStatement(); //creo  uno statement sulla coneesione
			   st.executeUpdate(sql); //faccio la query su uno statement
			   Random r = new Random();
			   this.funds = 1999999999 * r.nextDouble();
			   sql = "insert into cCard(username, fName, lName, date, number, cvv, funds) values ('" + username + "','" + firstName + "','" + lastName + "','" 
						+ expirationDate + "','" + number + "','" + CVV + "','" + funds + "')";
			   st.executeUpdate(sql);
		   }
		   catch (Exception e) {
			   throw new Exception(e.getMessage());
		   }
		   finally {
			   cn.close();
		   }
		
		
	}
	
	public double getFunds ()
	{
		return funds;
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
