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
	
	public static ImageIcon MAGICCIRCLE = new ImageIcon("./sprites/tiles/magiccircle.png");

	public static ImageIcon PLANTFL = new ImageIcon("./sprites/tiles/flowers/faceleft.png");
	public static ImageIcon PLANTBR = new ImageIcon("./sprites/tiles/flowers/backright.png");
	public static ImageIcon PLANTBL = new ImageIcon("./sprites/tiles/flowers/backleft.png");
	public static ImageIcon PLANTFR = new ImageIcon("./sprites/tiles/flowers/faceright.png");
	
	public static ImageIcon FENCEL = new ImageIcon("./sprites/tiles/FenceLeft1.png");
	public static ImageIcon FENCER = new ImageIcon("./sprites/tiles/FenceRight1.png");

	//Game Objects player can interact with

	public static ImageIcon ITEM_KEY = new ImageIcon("./sprites/items/key.png");

	public static ImageIcon DOOR = new ImageIcon("./sprites/tiles/door.png");

	public static ImageIcon ITEM_GOODPOTION = new ImageIcon("./sprites/items/goodpotion.png");

	public static ImageIcon ITEM_RARECANDY = new ImageIcon("./sprites/items/rarecandy.png");

	//Game enemies
	//Change here for print 4 direction
	public static ImageIcon RATTATA = new ImageIcon("./sprites/monster/Rattata/backright.png");
	public static ImageIcon RATTATABR = new ImageIcon("./sprites/monster/Rattata/backright.png");
	public static ImageIcon RATTATABL = new ImageIcon("./sprites/monster/Rattata/backleft.png");
	public static ImageIcon RATTATAFL = new ImageIcon("./sprites/monster/Rattata/faceleft.png");
	public static ImageIcon RATTATAFR = new ImageIcon("./sprites/monster/Rattata/faceright.png");
	
	public static ImageIcon RHYDONFL = new ImageIcon("./sprites/monster/Rhydon/faceleft.png");
	public static ImageIcon RHYDONBR = new ImageIcon("./sprites/monster/Rhydon/backright.png");
	public static ImageIcon RHYDONBL = new ImageIcon("./sprites/monster/Rhydon/backleft.png");
	public static ImageIcon RHYDONFR = new ImageIcon("./sprites/monster/Rhydon/faceright.png");
	
	public static ImageIcon ZUBATFL = new ImageIcon("./sprites/monster/Zubat/faceleft.png");
	public static ImageIcon ZUBATBR = new ImageIcon("./sprites/monster/Zubat/backright.png");
	public static ImageIcon ZUBATBL = new ImageIcon("./sprites/monster/Zubat/backleft.png");
	public static ImageIcon ZUBATFR = new ImageIcon("./sprites/monster/Zubat/faceright.png");

	public static ImageIcon MEWTWOFL = new ImageIcon("./sprites/monster/Mewtwo/faceleft.png");
	public static ImageIcon MEWTWOBR = new ImageIcon("./sprites/monster/Mewtwo/backright.png");
	public static ImageIcon MEWTWOBL = new ImageIcon("./sprites/monster/Mewtwo/backleft.png");
	public static ImageIcon MEWTWOFR = new ImageIcon("./sprites/monster/Mewtwo/faceright.png");


	public ImageIcon getSpriteImage();
}
