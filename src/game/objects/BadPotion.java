package game.objects;

import game.Player;

import javax.swing.ImageIcon;

public class BadPotion implements Item{
	private static final long serialVersionUID = 9081977774417525283L;
	
	private final int healthDamageAmount;
	
	public BadPotion(int healthDamageAmount){
		this.healthDamageAmount = healthDamageAmount;
	}
	
	@Override
	public String getName() {
		return "Good Potion";
	}

	@Override
	public String getDescription() {
		return "This potion smells funny";
	}
	
	public boolean isUsable(){
		return true;
	}

	@Override
	public ImageIcon getSpriteImage() {
		return null;
	}

	@Override
	public void useItem(Player player) {
		player.setHealth(healthDamageAmount);
	}
	
	

}
