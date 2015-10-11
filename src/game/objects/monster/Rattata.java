package game.objects.monster;

import game.Direction;
import game.objects.GameObject;

import javax.swing.ImageIcon;

/**
 * Represents a Rattata, the weakest enemy in the game
 * 
 *@author Sushant Balajee
 *@author Donald Tang
 *
 */

public class Rattata extends Monster{

	private static final long serialVersionUID = 6696995125055330253L;

	public int health;
	public int attack;
	public Direction direction = Direction.FACE_RIGHT;
	public Rattata(){
		spriteImage =GameObject.RATTATA;
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
		//change here for print 4 direction
		if(direction==Direction.BACK_LEFT){
			return GameObject.RATTATABL;
		}else if(direction==Direction.BACK_RIGHT){
			return GameObject.RATTATABR;
		}else if(direction==Direction.FACE_LEFT){
			return GameObject.RATTATAFL;
		}else {
			return GameObject.RATTATAFR;
		}
	}
	public void setDirection(Direction direction) {
		this.direction = direction;
	}

	@Override
	public Direction getDirection() {
		// TODO Auto-generated method stub
		return direction;
	}
}
