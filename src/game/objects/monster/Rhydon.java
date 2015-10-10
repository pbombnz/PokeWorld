package game.objects.monster;

import game.Direction;
import game.objects.GameObject;

import javax.swing.ImageIcon;

/**
 * Represents a Rhydon enemy, The second hardest enemy in the game
 * 
 *@author Sushant Balajee
 *@author Donald Tang
 */

public class Rhydon implements Monster{

	private static final long serialVersionUID = 6696995125055330253L;

	public int health;
	public int attack;
	public Direction direction = Direction.FACE_RIGHT;

	public Rhydon(){
	}
	
	public Rhydon(int attack, int health){
		this.attack = attack;
		this.health = health;
	}
	
	@Override
	public String getName() {
		return "Rhydon";
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
		return GameObject.RHYDON;
	}
	public void setDirection(Direction direction) {
		this.direction = direction;
	}
}
