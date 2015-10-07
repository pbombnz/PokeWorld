package game.objects;

import javax.swing.ImageIcon;

public class Door implements GameObject{

	private int id;
	public static enum LinkTo {GO_LAST_ROOM,GO_NEXT_ROOM};//for finding this door is link to last room or next room
	public LinkTo linkTo;
	
	public Door(int id,LinkTo linkTo){
		this.id = id;
		this.linkTo = linkTo;
	}
	@Override
	public ImageIcon getSpriteImage() {
		// TODO Auto-generated method stub
		return GameObject.WEAPON_GIANTSWORD;
	}
	
	public int id(){
		return id;
	}

}
