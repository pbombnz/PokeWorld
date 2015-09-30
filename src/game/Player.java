package game;

import game.avatar.Avatar;
import game.objects.Item;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;

public class Player implements Serializable {
	private static final long serialVersionUID = -4998808498802462674L;

	public static enum Direction { FACE_LEFT, FACE_RIGHT, BACK_LEFT, BACK_RIGHT};
	
	public static final int HEALTH = 100;
	public static final int ATTACK = 10;

	private Avatar avatar;
	
	private final String name;
	
	private int health;
	private int attack;

	private final int maxItems = 6;
	private List<Item> inventory = new ArrayList<Item>();

	private Location location;
	private Direction direction = Direction.FACE_RIGHT;

	public Player() {
		this.name = null;
	}
	
	public Player(String name) {
		this.name = name;
		this.attack = ATTACK;
		this.health = HEALTH;
	}

	public int getHealth(){
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

	public int getAttack(){
		return attack;
	}

	public void setAttack(int change){
		this.attack = change;
	}

	public List<Item> getInventory(){
		return inventory;
	}

	public boolean addToInventory(Item item){
		if (item == null){
			return false;
		}
		return inventory.add(item);
	}

	public Avatar getAvatar() {
		return avatar;
	}

	public void setAvatar(Avatar avatar) {
		this.avatar = avatar;
	}

	public Direction getDirection() {
		return direction;
	}

	public void setDirection(Direction direction) {
		this.direction = direction;
	}

	public String getName() {
		return name;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}
	
	public ImageIcon getSpriteBasedOnDirection() {
		if(avatar != null) {
			switch(direction) {
			case FACE_LEFT:
				return avatar.getFaceleft();
			case FACE_RIGHT:
				return avatar.getFaceright();
			case BACK_LEFT:
				return avatar.getBackleft();
			case BACK_RIGHT:
				return avatar.getBackright();
			}
		}
		return null;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((location == null) ? 0 : location.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Player other = (Player) obj;
		if (location == null) {
			if (other.location != null)
				return false;
		} else if (!location.equals(other.location))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	
}