package game.objects;

import javax.swing.ImageIcon;

/**
 * Represents a Zubat enemy, the second weakest enemy in the game
 * 
 *@author Sushant Balajee
 *@author Donald Tang
 */

public class Zubat implements Monster{

	private static final long serialVersionUID = 6696995125055330253L;

	public static enum Direction {FACE_LEFT, FACE_RIGHT, BACK_LEFT, BACK_RIGHT};

	public int health;
	public int attack;
	public Direction direction = Direction.FACE_RIGHT;

	public Zubat(int attack, int health){
		this.attack = attack;
		this.health = health;
	}

	@Override
	public String getName() {
		return "Zubat";
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
		return GameObject.ZUBAT;
	}
	public void setDirection(Direction direction) {
		this.direction = direction;
	}
}
