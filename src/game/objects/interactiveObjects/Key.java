package game.objects.interactiveObjects;

import game.Player;
import game.objects.GameObject;

import javax.swing.ImageIcon;

/**
 * Represents a Key in the game, has an ID used to check whether it can open
 * a door with matching ID value
 * 
 *@author Sushant Balajee
 *@author Donald Tang
 */

public class Key implements Item {
	
	private static final long serialVersionUID = 3007881710387627871L;

	private int id;//ID of key used to check which door it can open

	public Key(){
	}

	
	/**
	 * @param id, assigned an ID to open only one door of mathcing ID
	 */
	public Key(int id){
		this.id = id;
	}

	public int id(){
		return id;
	}
	@Override
	public String getName() {
		return "Key";
	}

	public boolean isUsable() {
		return false;
	}

	@Override
	public ImageIcon getSpriteImage() {
		return GameObject.ITEM_KEY;
	}

	@Override
	public String getDescription() {
		return "A Key used to open a door";
	}

	@Override
	public void useItem(Player player) {
		return;
	}

}
