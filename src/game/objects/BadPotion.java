package game.objects;

import java.awt.image.BufferedImage;

public class BadPotion implements Item{

	private final int hurt;
	private BufferedImage picture;
	
	public BadPotion(int hurt){
		this.hurt = hurt;
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
		return "This potion smells funny";
	}

	@Override
	public String imagePath() {
		return null;
	}
	
	public int getHurt(){
		return hurt;
	}
	
	public boolean isUsable(){
		return true;
	}
	
	

}
