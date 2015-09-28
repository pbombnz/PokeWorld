package player;

import gameObjects.Item;

import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;

/**
 *@author Wang Zhen
 */
public class MasterYi extends Player {

	public MasterYi(Location loc) {
		name = "Master Yi";
		attack = 100;
		health = 500;
		location = loc;
		faceleft = new ImageIcon("src/faceleft.png");
		faceright = new ImageIcon("src/faceright.png");
		backleft = new ImageIcon("src/backleft.png");
		backright = new ImageIcon("src/backright.png");
		touxiang = new ImageIcon("src/touxiang.png");
		direction = "fr";
	}

//	private static ImageIcon makeImageIcon(String filename) {
//		// using the URL means the image loads when stored
//		// in a jar or expanded into individual files.
//		java.net.URL imageURL = GameFrame.class.getResource(filename);
//		ImageIcon icon = null;
//		if (imageURL != null) {
//			icon = new ImageIcon(imageURL);
//		}
//		return icon;
//	}

	private List<Item> inventory = new ArrayList<Item>();

	public int getHealth() {
		return health;
	}

	public void updateHealth(int change) {
		health += change;
		if (health < 0) {
			health = 0;
		}
	}

	public boolean isDead() {
		return health <= 0;
	}

	public int getAttack() {
		return attack;
	}

	public void updateAttack(int change) {
		this.attack += change;
	}

	public List<Item> getInventory() {
		return inventory;
	}

	public boolean addToInventory(Item item) {
		if (item == null) {
			return false;
		}
		return inventory.add(item);
	}
}
