
package auction;
import utilities.*;
import java.util.ArrayList;

public class Item {
	private String name, description, imgPath;
	private int itemId;
	public Item(String name, String description, String image, int itemId) {
		this.name = name;
		this.description = description;
		this.imgPath = image;
		this.itemId = itemId;
	}	
	
	public int getItemId() {
		return itemId;
	}

	public String getName() {
		return name;
	}
	public String getDescription() {
		return description;
	}
	public String getImage() {
		return imgPath;
	}
}
