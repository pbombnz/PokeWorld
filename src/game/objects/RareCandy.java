package game.objects;

import game.Player;

import javax.swing.ImageIcon;

public class RareCandy implements Item {
	
	public RareCandy(){

	}
	
	@Override
	public String getName() {
		return "Rare Candy";
	}

	@Override
	public String getDescription() {
		return "Maybe this will help me evolve";
	}

	public boolean isUsable(){
		return true;
	}

	@Override
	public ImageIcon getSpriteImage() {
		return GameObject.ITEM_RARECANDY;
	}

	@Override
	public void useItem(Player player) {
		//level up
		
	}
}
