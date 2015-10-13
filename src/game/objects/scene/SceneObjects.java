package game.objects.scene;

import javax.swing.ImageIcon;

import game.Direction;
import game.objects.GameObject;

public abstract class SceneObjects implements GameObject {

	/**
	 * @return the direction of GameObject
	 */
	public abstract Direction getDirection();

	/**
	 * @param direction to be set
	 */
	public abstract void setDirection(Direction direction);

	public ImageIcon spriteImage;
	
}
