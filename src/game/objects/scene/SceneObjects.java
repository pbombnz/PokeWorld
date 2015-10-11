package game.objects.scene;

import javax.swing.ImageIcon;

import game.Direction;
import game.objects.GameObject;

public abstract class SceneObjects implements GameObject {

	public abstract Direction getDirection();

	public abstract void setDirection(Direction direction);

	public ImageIcon spriteImage;
	
}
