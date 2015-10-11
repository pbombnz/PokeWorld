package game.objects.monster;

import javax.swing.ImageIcon;

import game.Direction;
import game.objects.GameObject;

/**
 * An interface that is implemented by all the enemies in the game
 * holds all key information needed to fight the enemy and return a result
 * 
 *@author Sushant Balajee
 *@author Donald Tang
 *@author Wang Zhen
 */

public abstract class Monster implements GameObject{
	public ImageIcon spriteImage ;
	
	/**
	 * @return name of the enemy
	 */
	public abstract String getName();
	/**
	 * @return attack points of the enemy
	 */
	public abstract int attack();
	
	/**
	 * @return health of the enemy
	 */
	public abstract int getHealth();
	/**
	 * @param change, the amount of damage dealt to the enemy by the player
	 */
	public abstract void setHealth(int change);
	/**
	 * @return if the monster is dead
	 */
	public abstract boolean isDead();
	
	public abstract Direction getDirection();
	
	public abstract void setDirection(Direction direction);
	
}
