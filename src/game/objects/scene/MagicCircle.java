package game.objects.scene;

import game.Location;
import game.objects.GameObject;

import javax.swing.ImageIcon;

/**
 * Simple class used for the magic object on the board
 * which is a decorative feature, no interaction possible with this object
 * 
 *@author Zhen Wang
 */

public class MagicCircle implements GameObject {

	private static final long serialVersionUID = 4087421334363366222L;
	public final Location teleportLocation;

	public MagicCircle(Location location){
		this.teleportLocation = location;
	}	
	
	public Location getTeleportLocation() {
		return teleportLocation;
	}

//	public void setTeleportLocation(Location teleportLocation) {
//		this.teleportLocation = teleportLocation;
//	}

	@Override
	public ImageIcon getSpriteImage() {
		return GameObject.MAGICCIRCLE;
	}

}
