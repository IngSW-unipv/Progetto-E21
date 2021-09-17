package utilities;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ChatMessage {
	private String sender, receiver, text, time;
	
	public ChatMessage(String sender, String receiver, String text, String time) {
		this.sender = sender;
		this.receiver = receiver;
		this.text = text;
		this.time=time;
	}

	/**
	 * Saves a ChatMessage into DB
	 * @throws SQLException
	 */
	public void toDB() throws SQLException
	{
		Connection cn = DriverManager.getConnection("jdbc:mysql://sql11.freemysqlhosting.net:3306/sql11421731", "sql11421731", "83bkPjI9Yf");
		Statement st;
		ResultSet rs;
		String sql;
		try {
			sql = "insert into messages(sender, receiver, message, time) values ('" + sender + "','" + receiver + "','" + text + "','" + time + "')";
			st = cn.createStatement();
			st.execute(sql);
			System.out.println("inserted new message on the DB");
			System.out.println("connection terminated");
            
		} catch (SQLException e) {
			System.out.println("Error while connecting to the database");
		}
		   finally {
			   cn.close();
	     }	
	}

	public String getSender() {
		return sender;
	}

	public String getReceiver() {
		return receiver;
	}

	public String getText() {
		return text;
	}

	public String getTime() {
		return time;
	}

	public String output() {
		return "Sender: " + sender + " Receiver: " + receiver + "Text: " + text + "Time: " + time;

	}
	
	

}
