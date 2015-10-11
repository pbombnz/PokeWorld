package game.objects.scene;

import game.Direction;
import game.objects.GameObject;

import javax.swing.ImageIcon;

/**
 * Simple class used for the plant object on the board
 * which is a decorative feature, no interaction possible with this object
 * 
 *@author Sushant Balajee
 *@author Donald Tang
 */

public class Plant extends SceneObjects {

	private static final long serialVersionUID = 4087421334363366222L;
	public Direction direction = Direction.FACE_RIGHT;
	
	public Plant(){
	}		
	
	@Override
	public ImageIcon getSpriteImage() {
		if(direction==Direction.BACK_LEFT){
			return GameObject.PLANTBL;
		}else if(direction==Direction.BACK_RIGHT){
			return GameObject.PLANTBR;
		}else if(direction==Direction.FACE_LEFT){
			return GameObject.PLANTFL;
		}else {
			return GameObject.PLANTFR;
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
