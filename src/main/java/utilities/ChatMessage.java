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
	
	public ChatMessage(String sender, String receiver, String text) {
		this.sender = sender;
		this.receiver = receiver;
		this.text = text;
		LocalDateTime now = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm");
		String nowFormatted = now.format(formatter);
		this.time = nowFormatted;
	}
	
	public ChatMessage(String sender, String receiver, String text, String time) {
		this.sender = sender;
		this.receiver = receiver;
		this.text = text;
		this.time = time;
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
