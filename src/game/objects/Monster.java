package game.objects;

/**
 * An interface that is implemented by all the enemies in the game
 * holds all key information needed to fight the enemy and return a result
 * 
 *@author Sushant Balajee
 *@author Donald Tang
 */

public interface Monster extends GameObject{
	
	public static enum Direction { FACE_LEFT, FACE_RIGHT, BACK_LEFT, BACK_RIGHT};
	
	public String getName();
	public int attack();
	public int getHealth();
	/**
	 * @param change, the amount of damage dealt to the enemy by the player
	 */
	public void setHealth(int change);
	public boolean isDead();//check whether the monster is dead
}
