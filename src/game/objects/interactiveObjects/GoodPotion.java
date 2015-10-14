package game.objects.interactiveObjects;

import game.Player;
import game.objects.GameObject;

import javax.swing.ImageIcon;

public class GoodPotion implements Item {
	private static final long serialVersionUID = -6872048042823538745L;

	private final int healthHealAmount;

	public GoodPotion(){
		this.healthHealAmount = -1;
	}

	public GoodPotion(int healthHealAmount){
		this.healthHealAmount = healthHealAmount;
	}

	@Override
	public String getName() {
		return "Good Potion";
	}

	@Override
	public String getDescription() {
		return "This potion smells good";
	}

	/**
	 * @return the health it can heal
	 */
	public int getHealthHealAmount(){
		return healthHealAmount;
	}

	public boolean isUsable(){
		return true;
	}

	@Override
	public ImageIcon getSpriteImage() {
		return GameObject.ITEM_GOODPOTION;
	}

	@Override
	public void useItem(Player player) {
		player.setHealth(healthHealAmount);
	}

	@Override
	public int id() {
		return 0;
	}
}
