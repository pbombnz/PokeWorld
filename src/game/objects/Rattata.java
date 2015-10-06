package game.objects;

import javax.swing.ImageIcon;


import game.Location;
import game.Player.Direction;

/**
 *@author Wang Zhen
 */
public class Rattata implements Monster{
	public static enum Direction { FACE_LEFT, FACE_RIGHT, BACK_LEFT, BACK_RIGHT};
	public int health;
	public int attack;
	public Direction direction = Direction.FACE_RIGHT;
	public Rattata(int attack, int health){
		this.attack = attack;
		this.health = health;
	}
	
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "Rattata";
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
		return GameObject.RATTATA;
	}
	public void setDirection(Direction direction) {
		this.direction = direction;
	}
}
