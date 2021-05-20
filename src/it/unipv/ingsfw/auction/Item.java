package it.unipv.ingsfw.auction;
import java.util.ArrayList;
import it.unipv.ingsfw.utilities.Image;

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
