package game.objects;

import javax.swing.ImageIcon;

/**
 * Simple class used for the plant object on the board
 * which is a decorative feature, no interaction possible with this object
 * 
 *@author Sushant Balajee
 *@author Donald Tang
 */

public class Plant implements GameObject {

	private static final long serialVersionUID = 4087421334363366222L;

	@Override
	public ImageIcon getSpriteImage() {
		return GameObject.PLANT;
	}

}
