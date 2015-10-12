package game.objects.monster;

import game.Direction;
import game.objects.GameObject;

import javax.swing.ImageIcon;

/**
 * Represents a Mewtwo enemy in the game, the hardest enemy in the game
 * 
 *@author Sushant Balajee
 *@author Donald Tang
 */

public class Mewtwo extends Monster{

	private static final long serialVersionUID = 6696995125055330253L;

	public int health;
	public int attack;
	public Direction direction = Direction.FACE_RIGHT;

	public Mewtwo(){
		spriteImage =GameObject.MEWTWOFL;
	}

	public Mewtwo(int attack, int health){
		this.attack = attack;
		this.health = health;
	}

	@Override
	public String getName() {
		return "Mewtwo";
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
		if(direction==Direction.BACK_LEFT){
			return GameObject.MEWTWOBL;
		}else if(direction==Direction.BACK_RIGHT){
			return GameObject.MEWTWOBR;
		}else if(direction==Direction.FACE_LEFT){
			return GameObject.MEWTWOFL;
		}else {
			return GameObject.MEWTWOFR;
		}
	}

	@Override
	public void setDirection(Direction direction) {
		this.direction = direction;
	}

	@Override
	public Direction getDirection() {
		return direction;
	}
}
