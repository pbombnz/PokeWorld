package monster;

import javax.swing.ImageIcon;

import com.sun.jdi.Location;

public class Goblin extends Monster{
	public Goblin(Location loc){
		name = "Goblin";
		health = 200;
		attack = 4;
		this.location = loc;
		picture = new ImageIcon("src/faceleft.png");
	}
}