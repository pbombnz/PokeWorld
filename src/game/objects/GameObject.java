package game.objects;

import javax.swing.ImageIcon;

public interface GameObject {
	public static ImageIcon ITEM_KEY = new ImageIcon("./sprites/items/key.png");
	
	public static ImageIcon WEAPON_GIANTSWORD = new ImageIcon("./sprites/weapons/giantsword.png");
	
	public ImageIcon getSpriteImage();
}
