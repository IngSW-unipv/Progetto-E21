package auction;

import java.util.ArrayList;

public class Lot {
	
	private String name, description;
	private int lotId;
	private ArrayList<Item> items = new ArrayList<Item>();
	
	
	public Lot(String name, String description, int LotId) {
		this.name = name;
		this.description = description;
		this.lotId = lotId;
	}
	
	public int getLotId() {
		return lotId;
	}



	public Lot(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}


	public String getDescription() {
		return description;
	}

	public ArrayList<Item> getItems() {
		return items;
	}

	public void addItem(Item it) {
		items.add(it);
	}
	
	
	
	
}
