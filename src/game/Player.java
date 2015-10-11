package game;

import game.avatar.Avatar;
import game.objects.interactiveObjects.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;

public class Player implements Serializable {
	private static final long serialVersionUID = -3023304431184848781L;
	
	public static final int HEALTH = 10;
	public static final int ATTACK = 10;

	private Avatar avatar;
	
	private final int id;
	private final String name;
	
	private int health;
	private int attack;

	private int maxItems = 6;
	private List<Item> inventory = new ArrayList<Item>();

	private Location location;
	private Direction direction = Direction.FACE_RIGHT;

	private int playerLevel = 1;

	/**
	 * No-Args Constructor for Networking Serialisation (DO NOT USE)
	 */
	public Player() {
		this.id = -1;
		this.name = null;
	}
	
	public Player(int id, String name, Avatar avatar) {
		this.id = id;
		this.name = name;
		this.avatar = avatar;
		
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

	public void setInventory(List<Item> inventory) {
		this.inventory = inventory;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}
	
	public int getPlayerLevel(){
		return playerLevel;
	}
	
	public void setPlayerLevel(int change){
		this.playerLevel = change;
	}	

	public int getMaxItems() {
		return maxItems;
	}

	public void setMaxItems(int maxItems) {
		this.maxItems = maxItems;
	}

	public ImageIcon getSpriteBasedOnDirection() {
		if(avatar != null) {
			switch(direction) {
			case FACE_LEFT:
				return avatar.getCurrentEvolution(playerLevel).getFaceLeft();
			case FACE_RIGHT:
				return avatar.getCurrentEvolution(playerLevel).getFaceRight();
			case BACK_LEFT:
				return avatar.getCurrentEvolution(playerLevel).getBackLeft();
			case BACK_RIGHT:
				return avatar.getCurrentEvolution(playerLevel).getBackRight();
			}
		}
		throw new RuntimeException("The Avatar of the Player has not been set yet.");
	}
	
	public ImageIcon getSpriteBasedOnDirection(Direction fpsDirection) {
		if(avatar != null) {
			switch(fpsDirection) {
			case FACE_LEFT:
				return avatar.getCurrentEvolution(playerLevel).getFaceLeft();
			case FACE_RIGHT:
				return avatar.getCurrentEvolution(playerLevel).getFaceRight();
			case BACK_LEFT:
				return avatar.getCurrentEvolution(playerLevel).getBackLeft();
			case BACK_RIGHT:
				return avatar.getCurrentEvolution(playerLevel).getBackRight();
			}
		}
		throw new RuntimeException("The Avatar of the Player has not been set yet.");
	}

	public int getId() {
		return id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + attack;
		result = prime * result
				+ ((direction == null) ? 0 : direction.hashCode());
		result = prime * result + health;
		result = prime * result + id;
		result = prime * result
				+ ((inventory == null) ? 0 : inventory.hashCode());
		result = prime * result
				+ ((location == null) ? 0 : location.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + playerLevel;
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
		if (attack != other.attack)
			return false;
		if (direction != other.direction)
			return false;
		if (health != other.health)
			return false;
		if (id != other.id)
			return false;
		if (inventory == null) {
			if (other.inventory != null)
				return false;
		} else if (!inventory.equals(other.inventory))
			return false;
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
		if (playerLevel != other.playerLevel)
			return false;
		return true;
	}
}
