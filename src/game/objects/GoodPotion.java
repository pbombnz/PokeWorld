package game.objects;

import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

public class GoodPotion implements Item{

	private final int heal;
	private BufferedImage picture;
	
	public GoodPotion(int heal){
		this.heal = heal;
//		try{
//			picture = ImageIO.read(null);
//		} catch (IOException e){
//			e.printStackTrace();
//		}
	}
	
	@Override
	public String getName() {
		return "Good Potion";
	}

	@Override
	public String description() {
		return "This potion smells good";
	}

	@Override
	public String imagePath() {
		return null;
	}
	
	public int getHeal(){
		return heal;
	}
	
	public boolean isUsable(){
		return true;
	}
	
	

}
