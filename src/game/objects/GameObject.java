package game.objects;

import java.io.Serializable;

import javax.swing.ImageIcon;

/**
 * All objects in the game implement GameObject
 * contains the image sprites for all the objects
 * 
 *@author Sushant Balajee
 *@author Donald Tang
 */

public interface GameObject extends Serializable {
	
	//Game detailing
	
	public static ImageIcon TREE = new ImageIcon("./sprites/tiles/tree.png");

	public static ImageIcon PLANT = new ImageIcon("./sprites/tiles/flowers/faceleft.png");
	
	public static ImageIcon FENCE_LEFT = new ImageIcon("./sprites/tiles/FenceLeft1.png");
	
	public static ImageIcon FENCE_RIGHT = new ImageIcon("./sprites/tiles/FenceRight1.png");

	//Game Objects player can iteract with

	public static ImageIcon ITEM_KEY = new ImageIcon("./sprites/items/key.png");

	public static ImageIcon DOOR = new ImageIcon("./sprites/tiles/door.png");

	public static ImageIcon ITEM_GOODPOTION = new ImageIcon("./sprites/items/goodpotion.png");

	public static ImageIcon ITEM_RARECANDY = new ImageIcon("./sprites/items/rarecandy.png");

	//Game enemies
	
	public static ImageIcon RATTATA = new ImageIcon("./sprites/monster/Rattata/backright.png");

	public static ImageIcon RHYDON = new ImageIcon("./sprites/monster/Rhydon/faceleft.png");

	public static ImageIcon ZUBAT = new ImageIcon("./sprites/monster/Zubat/faceleft.png");

	public static ImageIcon MEWTWO = new ImageIcon("./sprites/monster/Mewtwo/faceleft.png");


	public ImageIcon getSpriteImage();
}
