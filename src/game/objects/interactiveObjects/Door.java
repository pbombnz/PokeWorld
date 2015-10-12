package game.objects.interactiveObjects;

import game.objects.GameObject;

import javax.swing.ImageIcon;

import rooms.Room;

/**
 * This class represents the Door object in the game, the player must 
 * have a key which has a matching ID to that of the door they are trying to open
 * 
 * 
 * @author Sushant Balajee
 * @author Donald Tang
 * @contrib Donald Tang
 */

public class Door implements GameObject{
	
	private static final long serialVersionUID = -3108639527814191048L;
	private int id;
	private int nextRoom;

	public Door(){
	}
	
	/**
	 * @param id, ID of the door
	 * @param nextRoom The room this door goes to
	 */
	public Door(int id, int nextRoom){
		this.id = id;
		this.nextRoom = nextRoom;
	}

	@Override
	public ImageIcon getSpriteImage() {
		return GameObject.DOOR;
	}

	/**
	 * @return the id of the door
	 */
	public int id(){
		return id;
	}

	public int getNextRoom() {
		return nextRoom;
	}
}
