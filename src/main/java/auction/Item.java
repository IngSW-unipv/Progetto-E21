
package auction;
import utilities.*;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;

public class Item {
	private String name, description, imgPath;
	private int itemId;
	InputStream img;
	public Item(String name, String description, String image, int itemId) {
		this.name = name;
		this.description = description;
		this.imgPath = image;
		this.itemId = itemId;
	}	
	
	public Item(String name, String description, String image, InputStream img) {
		this.name = name;
		this.description = description;
		this.imgPath = image;
		this.img = img;
	}	
	
	public InputStream getImgFile()
	{
		return img;
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
