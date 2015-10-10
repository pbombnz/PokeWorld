package game.objects.monster;

import game.objects.GameObject;

/**
 * An interface that is implemented by all the enemies in the game
 * holds all key information needed to fight the enemy and return a result
 * 
 *@author Sushant Balajee
 *@author Donald Tang
 */

public interface Monster extends GameObject{
	/**
	 * @return name of the enemy
	 */
	public String getName();
	/**
	 * @return attack points of the enemy
	 */
	public int attack();
	
	/**
	 * @return health of the enemy
	 */
	public int getHealth();
	/**
	 * @param change, the amount of damage dealt to the enemy by the player
	 */
	public void setHealth(int change);
	/**
	 * @return if the monster is dead
	 */
	public boolean isDead();
}
