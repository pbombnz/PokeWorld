package game.objects.scene;

import game.objects.GameObject;

import javax.swing.ImageIcon;

/**
 * Simple class used for the tree object on the board
 * which is a decorative feature, no interaction possible with this object
 * 
 *@author Sushant Balajee
 *@author Donald Tang
 */

public class Tree implements GameObject {

	private static final long serialVersionUID = 4087421334363366222L;

	public Tree(){
	}	
	
	@Override
	public ImageIcon getSpriteImage() {
		return GameObject.TREE;
	}

}
