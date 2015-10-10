package game.objects.monster;

import game.Direction;
import game.objects.GameObject;

import javax.swing.ImageIcon;

/**
 * Represents a Rattata, the weakest enemy in the game
 * 
 *@author Sushant Balajee
 *@author Donald Tang
 */

public class Rattata implements Monster{

	private static final long serialVersionUID = 6696995125055330253L;

	public int health;
	public int attack;
	public Direction direction = Direction.FACE_RIGHT;

	public Rattata(){
	}	
	
	public Rattata(int attack, int health){
		this.attack = attack;
		this.health = health;
	}
	
	@Override
	public String getName() {
		return "Rattata";
	}

	@Override
	public int attack() {
		return attack;
	}

	@Override
	public int getHealth() {
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
		return GameObject.RATTATA;
	}
	public void setDirection(Direction direction) {
		this.direction = direction;
	}
}
