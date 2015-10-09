package game.objects.interactiveObjects;

import game.Player;
import game.objects.GameObject;

import javax.swing.ImageIcon;

/**
 * An item that can be picked up and is used straight away
 * Picking up this item will increase the players level by 1
 * 
 *@author Sushant Balajee
 *@author Donald Tang
 */

public class RareCandy implements Item {

	private static final long serialVersionUID = 5655585108741040609L;
	
	int currLevel;//current level of the player character

	@Override
	public String getName() {
		return "Rare Candy";
	}

	@Override
	public String getDescription() {
		return "Maybe this will help me evolve";
	}

	public boolean isUsable(){
		return true;
	}

	@Override
	public ImageIcon getSpriteImage() {
		return GameObject.ITEM_RARECANDY;
	}

	@Override
	public void useItem(Player player) {
		//level up
	}

	public int level(){
		return currLevel + 1;
	}

	@Override
	public int id() {
		return 0;
	}
}
