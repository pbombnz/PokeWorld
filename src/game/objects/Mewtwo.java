package game.objects;

import javax.swing.ImageIcon;

/**
 *@author Wang Zhen
 *@author Sushant Balajee
 */
public class Mewtwo implements Monster{

	private static final long serialVersionUID = 6696995125055330253L;
	
	public static enum Direction { FACE_LEFT, FACE_RIGHT, BACK_LEFT, BACK_RIGHT};
	public int health;
	public int attack;
	public Direction direction = Direction.FACE_RIGHT;
	public Mewtwo(int attack, int health){
		this.attack = attack;
		this.health = health;
	}
	
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "Rhydon";
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
		return GameObject.MEWTWO;
	}
	public void setDirection(Direction direction) {
		this.direction = direction;
	}
}
