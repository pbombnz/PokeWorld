package gameObjects;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;


public class Key implements Item{

	private final int id;
	private BufferedImage picture;


	public Key(int id){
		this.id = id;
		//		try {
		//			picture = ImageIO.read(PanelRender.class
		//					.getResource("/images/Key.png"));
		//		} catch (IOException e) {
		//			e.printStackTrace();
		//		}
	}

	@Override
	public String getName() {
		return "Key";
	}

	@Override
	public String description() {
		return null;
	}

	@Override
	public String imagePath() {
		return null;
	}


	public int getObjectID() {
		return id;
	}

	public void draw(Graphics g) {
		g.drawImage(picture, 0, 0, null);
	}

	public boolean isUsable() {
		return false;
	}

	public boolean use() {
		return false;
	}

}
