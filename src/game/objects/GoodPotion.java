package game.objects;

import game.Player;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class GoodPotion implements Item {
	private final int healthHealAmount;
	
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
	
	public int getHealthHealAmount(){
		return healthHealAmount;
	}
	
	public boolean isUsable(){
		return true;
	}

	@Override
	public ImageIcon getSpriteImage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void useItem(Player player) {
		player.setHealth(healthHealAmount);
	}
}
