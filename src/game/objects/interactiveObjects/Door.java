package game.objects.interactiveObjects;

import game.objects.GameObject;

import javax.swing.ImageIcon;

import rooms.Room;

/**
 * This class represents the Door object in the game, the player must 
 * have a key which has a matching ID to that of the door they are trying to open
 * 
 * @author Sushant Balajee
 * @author Donald Tang
 * @author Prashant Bhikhu
 * @author Wang Zhen
 */

public class Door implements GameObject {

	private static final long serialVersionUID = -3108639527814191048L;
	private int id;
	private int nextRoom;
	private int nextRoomX;//the position you want to go in nextroom
	private int nextRoomY;

	public Door() {
	}

	/**
	 * @param id, ID of the door
	 * @param nextRoom The room this door goes to
	 */
	public Door(int id, int nextRoom, int nextRoomX, int nextRoomY) {
		this.id = id;
		this.nextRoom = nextRoom;
		this.nextRoomX = nextRoomX;
		this.nextRoomY = nextRoomY;
	}

	/**
	 * @return the next room that is x
	 */
	public int getNextRoomX() {
		return nextRoomX;
	}

	/**
	 * @param nextRoomX, set the next room at x
	 */
	public void setNextRoomX(int nextRoomX) {
		this.nextRoomX = nextRoomX;
	}

	/**
	 * @return the next room at y
	 */
	public int getNextRoomY() {
		return nextRoomY;
	}

	/**
	 * @param nextRoomY, set the next room at y
	 */
	public void setNextRoomY(int nextRoomY) {
		this.nextRoomY = nextRoomY;
	}

	
	@Override
	public ImageIcon getSpriteImage() {
		return GameObject.DOOR;
	}

	/**
	 * @return the id of the door
	 */
	public int id() {
		return id;
	}

	/**
	 * @return the next room
	 */
	public int getNextRoom() {
		return nextRoom;
	}
}
