package game.objects;

import game.Player;

import javax.swing.ImageIcon;


public class Key implements Item {
	private static final long serialVersionUID = 3007881710387627871L;

	@Override
	public String getName() {
		return "Key";
	}

	public boolean isUsable() {
		return false;
	}

	@Override
	public ImageIcon getSpriteImage() {
		return GameObject.ITEM_KEY;
	}

	@Override
	public String getDescription() {
		return "A Key";
	}

	@Override
	public void useItem(Player player) {
		return;
	}

}
