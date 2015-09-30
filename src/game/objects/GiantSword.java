package game.objects;

import javax.swing.ImageIcon;

public class GiantSword implements Weapon {
	private static final long serialVersionUID = 1751235644109188989L;

	@Override
	public ImageIcon getSpriteImage() {
		return GameObject.WEAPON_GIANTSWORD;
	}

	@Override
	public String getName() {
		return "Giant Sword";
	}

	@Override
	public String getDescription() {
		return "A Giant Sword that looks very dangerous";
	}

	@Override
	public int getAttackDamage() {
		return 10;
	}

}
