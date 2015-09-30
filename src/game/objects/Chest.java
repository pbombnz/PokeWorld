package game.objects;

import game.Player;

import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;


public class Chest implements Item {
	private static final long serialVersionUID = -5905703818196371005L;
	
	private boolean isOpened = false;
	private Item itemInsideChest;

	public Chest(Item itemInsideChest) {
		this.itemInsideChest = itemInsideChest;
	}

	@Override
	public ImageIcon getSpriteImage() {
		return null;
	}

	@Override
	public String getName() {
		return "A Chest";
	}

	@Override
	public String getDescription() {
		return "A Chest with possibly something in it";
	}

	@Override
	public void useItem(Player player) {
		if(isOpened) {
			
		} else {
			
			isOpened = true;
		}
	}

	@Override
	public boolean isUsable() {
		return true;
	}
	
}
