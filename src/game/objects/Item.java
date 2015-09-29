package game.objects;

import game.Player;

public interface Item extends GameObject {
	public String getName();
	public String getDescription();
	
	public void useItem(Player player);
	public boolean isUsable();
}
