package game.objects;

import java.io.Serializable;

import javax.swing.ImageIcon;

public interface GameObject extends Serializable {
	public static ImageIcon ITEM_KEY = new ImageIcon("./sprites/items/key.png");
	
	public static ImageIcon WEAPON_GIANTSWORD = new ImageIcon("./sprites/weapons/giantsword.png");

	public static ImageIcon ITEM_GOODPOTION = new ImageIcon("./sprites/items/goodpotion.png");
	
	public static ImageIcon GOBLIN = new ImageIcon("./sprites/monster/Rattata/backright.png");

	public static ImageIcon TREE = new ImageIcon("./sprites/tiles/tree.png");
	public static ImageIcon ITEM_RARECANDY = new ImageIcon("./sprites/items/rarecandy.png");
	public ImageIcon getSpriteImage();
}
