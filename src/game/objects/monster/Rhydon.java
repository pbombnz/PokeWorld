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

public class Rhydon extends Monster{

	private static final long serialVersionUID = 6696995125055330253L;

	public int health;
	public int attack;
	public Direction direction = Direction.FACE_RIGHT;

	public Rhydon(){
		spriteImage=GameObject.RHYDONFL;
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
		if(direction==Direction.BACK_LEFT){
			return GameObject.RHYDONBL;
		}else if(direction==Direction.BACK_RIGHT){
			return GameObject.RHYDONBR;
		}else if(direction==Direction.FACE_LEFT){
			return GameObject.RHYDONFL;
		}else {
			return GameObject.RHYDONFR;
		}
	}
	public void setDirection(Direction direction) {
		this.direction = direction;
	}

	@Override
	public Direction getDirection() {
		return direction;
	}
}
