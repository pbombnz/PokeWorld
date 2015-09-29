package gameObjects;

public interface Weapon extends GameObject {

	public String getName();
	
	public String imagePath();
	
	public String description();
	
	public int attackDamage();
	
}
