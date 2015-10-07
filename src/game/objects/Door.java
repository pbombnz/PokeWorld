package game.objects;

import javax.swing.ImageIcon;

public class Door implements GameObject{

	private int id;
	public int linkTo;
	public int linkFrom;
	
	public Door(int id,int linkFrom,int linkTo){
		this.id = id;
		this.linkTo = linkTo;
		this.linkFrom = linkFrom;
	}
	@Override
	public ImageIcon getSpriteImage() {
		// TODO Auto-generated method stub
		return GameObject.DOOR;
	}
	
	public int id(){
		return id;
	}

}
