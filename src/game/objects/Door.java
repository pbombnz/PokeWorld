package game.objects;

import javax.swing.ImageIcon;

public class Door implements GameObject{

	private int id;
	
	public Door(int id){
		this.id = id;
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
