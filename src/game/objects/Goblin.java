package game.objects;

import javax.swing.ImageIcon;

import game.Location;

/**
 *@author Wang Zhen
 */
public class Goblin implements Monster{

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "Goblin";
	}

	@Override
	public int attack() {
		// TODO Auto-generated method stub
		return 10;
	}

	@Override
	public int health() {
		// TODO Auto-generated method stub
		return 50;
	}

	@Override
	public ImageIcon getSpriteImage() {
		// TODO Auto-generated method stub
		return GameObject.GOBLIN;
	}

}
