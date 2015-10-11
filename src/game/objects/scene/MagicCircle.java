package game.objects.scene;

import game.objects.GameObject;

import javax.swing.ImageIcon;

/**
 * Simple class used for the magic object on the board
 * which is a decorative feature, no interaction possible with this object
 * 
 *@author Zhen Wang
 */

public class MagicCircle implements GameObject {

	private static final long serialVersionUID = 4087421334363366223L;
	public int teleportX;
	public int teleportY;

	public MagicCircle(int x,int y) {
		this.teleportX =x;
		this.teleportY = y;
	}

	public int getTeleportX() {
		return teleportX;
	}

	public void setTeleportX(int teleportX) {
		this.teleportX = teleportX;
	}

	public int getTeleportY() {
		return teleportY;
	}

	public void setTeleportY(int teleportY) {
		this.teleportY = teleportY;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public ImageIcon getSpriteImage() {
		return GameObject.MAGICCIRCLE;
	}

}
