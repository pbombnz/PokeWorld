package monster;

import javax.swing.ImageIcon;

import game.Location;

/**
 *@author Wang Zhen
 */
public class Goblin extends Monster{
	public Goblin(Location loc){
		name = "Goblin";
		health = 200;
		attack = 4;
		this.location = loc;
		picture = new ImageIcon("src/faceleft.png");
	}
}
