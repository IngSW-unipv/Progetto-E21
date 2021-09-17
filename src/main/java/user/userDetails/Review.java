package user.userDetails;

/**
 * This class describes a Review given to a user
 */
public class Review {
	private String sender, receiver, text, img;

	/**
	 * Review constructor
	 * @param sender The sender of the Review
	 * @param receiver The user that gets the Review
	 * @param text String text of the Review
	 * @param img Image of the sender
	 */
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
