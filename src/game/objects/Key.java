package game.objects;

import game.Player;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;


public class Key implements Item {
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
