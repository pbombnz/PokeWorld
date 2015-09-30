package game.objects;

import javax.swing.ImageIcon;

public class Dagger implements Weapon {
	private static final long serialVersionUID = -1252162793041162040L;

	@Override
	public String getName() {
		return "Dagger";
	}

	@Override
	public ImageIcon getSpriteImage() {
		return null;
	}

	@Override
	public String getDescription() {
		return null;
	}

	@Override
	public int getAttackDamage() {
		return 2;
	}

}
