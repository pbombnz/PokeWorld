package game.objects.scene;

import game.Direction;
import game.objects.GameObject;

import javax.swing.ImageIcon;

/**
 * Simple class used for the fence object on the board
 * which is a decorative feature, no interaction possible with this object
 * 
 *@author Sushant Balajee
 *@author Donald Tang
 */

public class Fence extends SceneObjects{

	private static final long serialVersionUID = -5093601402600649692L;
	public Direction direction = Direction.FACE_RIGHT;
	
	public Fence(){
	}	
	
	@Override
	public ImageIcon getSpriteImage() {
		if(direction==Direction.BACK_LEFT){
			return GameObject.FENCEL;
		}else if(direction==Direction.BACK_RIGHT){
			return GameObject.FENCER;
		}else if(direction==Direction.FACE_LEFT){
			return GameObject.FENCER;
		}else {
			return GameObject.FENCEL;
		}
	}

	@Override
	public Direction getDirection() {
		return direction;
	}

	@Override
	public void setDirection(Direction direction) {
		this.direction = direction;
	}

}

