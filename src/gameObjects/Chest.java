package gameObjects;

import java.util.ArrayList;
import java.util.List;


public class Chest {

	private final int id;
	private List<Item> inventory = new ArrayList<Item>();

	public Chest(int ID, List<Item> inv) {
		this.id = ID;
		this.inventory = inv;
	}

	public List<Item> getLoot() {
		return inventory;
	}

	public int getObjectID() {
		return id;
	}

	public List<Item> openChest() {
		return inventory;
	}

	public void updateLoot(List<Item> items) {
		this.inventory = items;
	}

	public void removeInv() {
		this.inventory = new ArrayList<Item>();
	}
	
}
