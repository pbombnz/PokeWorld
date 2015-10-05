package game.objects;

import javax.swing.ImageIcon;

import game.Location;

/**
 *@author Wang Zhen
 */
public interface Monster extends GameObject{
	public String getName();
	public int attack();
	public int getHealth();
	public void setHealth(int change);
	public boolean isDead();
}
