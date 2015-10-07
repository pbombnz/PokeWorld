package game.objects;

import java.io.Serializable;

import javax.swing.ImageIcon;

public interface GameObject extends Serializable {
	public static ImageIcon ITEM_KEY = new ImageIcon("./sprites/items/key.png");
	
	public static ImageIcon DOOR = new ImageIcon("./sprites/tiles/door.png");

	public static ImageIcon ITEM_GOODPOTION = new ImageIcon("./sprites/items/goodpotion.png");
	
	public static ImageIcon RATTATA = new ImageIcon("./sprites/monster/Rattata/backright.png");
	
	public static ImageIcon RHYDON = new ImageIcon("./sprites/monster/Rhydon/faceleft.png");
	
	public static ImageIcon ZUBAT = new ImageIcon("./sprites/monster/Zubat/faceleft.png");
	
	public static ImageIcon MEWTWO = new ImageIcon("./sprites/monster/Mewtwo/faceleft.png");

	public static ImageIcon TREE = new ImageIcon("./sprites/tiles/tree.png");
	
	public static ImageIcon PLANT = new ImageIcon("./sprites/tiles/flow.png");
	
	public static ImageIcon MUSHROOM = new ImageIcon("./sprites/tiles/rock1.png");
	
	public static ImageIcon ITEM_RARECANDY = new ImageIcon("./sprites/items/rarecandy.png");
	public ImageIcon getSpriteImage();
}
