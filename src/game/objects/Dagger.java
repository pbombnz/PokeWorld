package game.objects;

import javax.swing.ImageIcon;

public class Dagger implements Weapon{

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
