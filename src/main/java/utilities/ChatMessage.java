package utilities;

public class ChatMessage {
	private String sender, receiver, text, time;
	
	public ChatMessage(String sender, String receiver, String text, String time) {
		this.sender = sender;
		this.receiver = receiver;
		this.text = text;
		this.time=time;
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
	
	

}
