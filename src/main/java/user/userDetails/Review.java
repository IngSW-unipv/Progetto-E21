package user.userDetails;

public class Review {
	private String sender, receiver, text, img;
	
	public Review(String sender, String receiver, String text, String img) {
		this.sender = sender;
		this.receiver = receiver;
		this.text = text;
		this.img = img;
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

	public String getImg() {
		return img;
	}
	
	
	
}
