package game;

import game.avatar.Avatar;
import game.objects.interactiveObjects.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;

/* 
 * @authors Sushant Balajee, Donald Tang,
 * @contributors Prashant Bhikhu
 * 
 * This class represents the player of the game, it holds 
 * all key information about the player, their inventory, stats etc.
 * All player stats are initialised here
 */
public class Player implements Serializable {
	private static final long serialVersionUID = -3023304431184848781L;

	public static final int HEALTH = 10;
	public static final int ATTACK = 10;
	public static final int MAX_PLAYER_LEVEL = 3;
	private Avatar avatar;

	private int id;
	private String name;

	private int health;
	private int attack;

	private int maxItems = 6;
	private List<Item> inventory = new ArrayList<Item>();

	private Location location;
	private Direction direction = Direction.FACE_RIGHT;

	private int playerLevel = 1;

	/**
	 * No-Args Constructor for Networking Serialization (DO NOT USE)
	 */
	public Player() {
	}

	/**
	 * @param id,
	 *            players id
	 * @param name,
	 *            players name
	 * @param avatar,
	 *            players avatar character
	 */
	public Player(int id, String name, Avatar avatar) {
		this.id = id;
		this.name = name;
		this.avatar = avatar;
		this.attack = ATTACK;
		this.health = HEALTH;
	}

	/**
	 * 
	 * @return int, players heatlh
	 */

	public int getHealth() {
		return health;
	}

	/**
	 * @param change,
	 *            amount of health changed by
	 */
	public void setHealth(int change) {
		health = change;
		if (health < 0) {
			health = 0;
		}
	}

	/**
	 * 
	 * @return boolean, players life status
	 */
	public boolean isDead() {
		return health <= 0;
	}

	/**
	 * 
	 * @return int, attack damage
	 */
	public int getAttack() {
		return attack;
	}

	/**
	 * @param change,
	 *            amount of attack changed by
	 */
	public void setAttack(int change) {
		this.attack = change;
	}

	/**
	 * 
	 * @return List<Item>, player inventory
	 */
	public List<Item> getInventory() {
		return inventory;
	}

	/**
	 * @param item,
	 *            the item being added to the inventory
	 * @return
	 */
	public boolean addToInventory(Item item) {
		if (item == null) {
			return false;
		}
		return inventory.add(item);
	}

	/**
	 * 
	 * @return Avatar, players avatar
	 */
	public Avatar getAvatar() {
		return avatar;
	}

	/**
	 * @param avatar,
	 *            the character chosen by the player to represent them
	 */
	public void setAvatar(Avatar avatar) {
		this.avatar = avatar;
	}

	/**
	 * 
	 * @return Direction, players direction
	 */
	public Direction getDirection() {
		return direction;
	}

	/**
	 * 
	 * @param direction,
	 *            direction to set
	 */
	public void setDirection(Direction direction) {
		this.direction = direction;
	}

	/**
	 * 
	 * @param id,
	 *            used to set unique id
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * 
	 * @param name,
	 *            sets player name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 
	 * @return String, players name
	 */
	public String getName() {
		return name;
	}

	/**
	 * 
	 * @param inventory,
	 *            sets player inventory
	 */
	public void setInventory(List<Item> inventory) {
		this.inventory = inventory;
	}

	/**
	 * 
	 * @return Location, players location
	 */
	public Location getLocation() {
		return location;
	}

	/**
	 * 
	 * @param location,
	 *            sets player location
	 */
	public void setLocation(Location location) {
		this.location = location;
	}

	/**
	 * 
	 * @return int, players level
	 */
	public int getPlayerLevel() {
		return playerLevel;
	}

	/**
	 * @param change,
	 *            the level of the player character
	 */
	public void setPlayerLevel(int change) {
		this.playerLevel = change;
	}

	/**
	 * 
	 * @return int, max number of items player can hold
	 */
	public int getMaxItems() {
		return maxItems;
	}

	/**
	 * @param maxItems,
	 *            6 items max held by the inventory
	 */
	public void setMaxItems(int maxItems) {
		this.maxItems = maxItems;
	}
	/**
	 * 
	 * @return ImageIcon, based on players direction
	 */
	public ImageIcon getSpriteBasedOnDirection() {
		if (avatar != null) {
			switch (direction) {
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
	/**
	 * @param fpsDirection, players direction
	 * @return ImageIcon, based on players direction
	 */
	public ImageIcon getSpriteBasedOnDirection(Direction fpsDirection) {
		if (avatar != null) {
			switch (fpsDirection) {
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
/**
 * 
 * @return int, players id
 */
	public int getId() {
		return id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + attack;
		result = prime * result + ((direction == null) ? 0 : direction.hashCode());
		result = prime * result + health;
		result = prime * result + id;
		result = prime * result + ((inventory == null) ? 0 : inventory.hashCode());
		result = prime * result + ((location == null) ? 0 : location.hashCode());
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
