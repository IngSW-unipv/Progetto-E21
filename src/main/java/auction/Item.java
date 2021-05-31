
package auction;
import utilities.*;
import java.util.ArrayList;

public class Item {
	private String name, descrizione;
	ArrayList<Image> image;
	public Item(String name, String descrizione, ArrayList<Image> image) {
		this.name = name;
		this.descrizione = descrizione;
		this.image = image;
	}
	public String getName() {
		return name;
	}
	public String getDescrizione() {
		return descrizione;
	}
	public ArrayList<Image> getImage() {
		return image;
	}
}
