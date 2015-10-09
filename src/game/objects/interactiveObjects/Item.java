package game.objects.interactiveObjects;

import game.Player;
import game.objects.GameObject;

/**
 * An interface used by objects in the game 
 * Only objects that can be picked up implement Item
 * 
 *@author Sushant Balajee
 *@author Donald Tang
 */

public interface Item extends GameObject {
	
	public String getName();
	public String getDescription();
	
	public void useItem(Player player);
	public boolean isUsable();
	/**
	 * @return id of the item, used for the Key
	 */
	public int id();
}
