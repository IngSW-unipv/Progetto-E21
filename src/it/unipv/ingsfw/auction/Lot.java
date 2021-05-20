package it.unipv.ingsfw.auction;

import java.util.ArrayList;

public class Lot {
	
	private String name;
	ArrayList<Item> item;
	
	public Lot(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}


	public ArrayList<Item> getItem() {
		return item;
	}

	public void addItem(Item item) {
		this.item.add(item);
	}
	
	
	
	
}
