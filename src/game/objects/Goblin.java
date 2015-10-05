package game.objects;

import javax.swing.ImageIcon;


import game.Location;

/**
 *@author Wang Zhen
 */
public class Goblin implements Monster{
	
	public int health;
	public int attack;

	public Goblin(int attack, int health){
		this.attack = attack;
		this.health = health;
	}
	
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "Goblin";
	}

	@Override
	public int attack() {
		// TODO Auto-generated method stub
		return attack;
	}

	@Override
	public int getHealth() {
		// TODO Auto-generated method stub
		return health;
	}
	
	public void setHealth(int change){
		health = change;
		if(health < 0){
			health = 0;
		}
	}

	public boolean isDead(){
		return health <= 0;
	}
	
	@Override
	public ImageIcon getSpriteImage() {
		// TODO Auto-generated method stub
		return GameObject.GOBLIN;
	}

}
