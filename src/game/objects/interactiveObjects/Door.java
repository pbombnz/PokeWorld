package game.objects.interactiveObjects;

import game.objects.GameObject;

import javax.swing.ImageIcon;

/**
 * This class represents the Door object in the game, the player must 
 * have a key which has a matching ID to that of the door they are trying to open
 * 
 * 
 *@author Sushant Balajee
 *@author Donald Tang
 */

public class Door implements GameObject{
	
	private static final long serialVersionUID = -3108639527814191048L;
	private int id;
	public int linkTo;
	public int linkFrom;

	public Door(){
	}
	
	/**
	 * @param id, ID of the door
	 * @param linkFrom, The room this door goes to
	 * @param linkTo, The room this door is from
	 */
	public Door(int id,int linkFrom,int linkTo){
		this.id = id;
		this.linkTo = linkTo;
		this.linkFrom = linkFrom;
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

}
