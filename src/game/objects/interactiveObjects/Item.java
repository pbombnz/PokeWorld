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
	
	/**
	 * @return name of item
	 */
	public String getName();
	
	/**
	 * @return description of item
	 */
	public String getDescription();
	
	/**
	 * @param player uses the item on player
	 */
	public void useItem(Player player);
	
	/**
	 * @return if the item is usable
	 */
	public boolean isUsable();
	
	/**
	 * @return The ID of the item (used for the Key)
	 */
	public int id();
}
