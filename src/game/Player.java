package game;

import game.avatar.Avatar;
import game.objects.Item;

import java.util.ArrayList;
import java.util.List;

public class Player {
	public static enum Direction { FACE_LEFT, FACE_RIGHT, BACK_LEFT, BACK_RIGHT};
	
	public static final int HEALTH = 100;
	public static final int ATTACK = 10;

	private Avatar avatar;
	
	private final int id;
	private final String name;
	
	private int health;
	private int attack;

	private final int maxItems = 6;
	private List<Item> inventory = new ArrayList<Item>();

	private Location location;
	private Direction direction = Direction.FACE_RIGHT;

	public Player(int id, String name, int attack, int health, List<Item> items){
		this.id = id;
		this.name = name;
		this.attack = attack;
		this.health = health;
		if(items != null){
			this.inventory.addAll(items);
		}
	}

	public int getHealth(){
		return health;
	}

	public void setHealth(int change){
		health += change;
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

	public void updateAttack(int change){
		this.attack += change;
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

	public int getId() {
		return id;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}
}