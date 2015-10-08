package game.objects;

import game.Player;

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
	public int id();
}
