package Storage;

import java.util.List;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.json.simple.*;

import game.Location;
import game.Player;
import game.Player.Direction;
import game.avatar.Avatar;
import game.objects.interactiveObjects.*;

/*
 * a class that saves the player's current state to a json file
 * 
 * stores all the major variables in the player class
 * 
 * @author Priyanka Bhula
 */
public class GameToJson {

	
	//@SuppressWarnings("unchecked")
	public static void savePlayer(Player p) throws IOException, InvalidSaveException{
		JSONObject jsonobj = new JSONObject();
		
		String name = p.getName();
		Integer attack = p.getAttack();
		String avatar = p.getAvatar().getName();
		int dir = p.getDirection().ordinal();
		int health = p.getHealth();
		List<Item> items = p.getInventory();
		String loc = p.getLocation().toString();	
		int locX = p.getLocation().getX();
		int locY = p.getLocation().getY();
		String roomName = p.getLocation().getRoom().getName();
			
		jsonobj.put("name", name);
		jsonobj.put("attack",attack);
		jsonobj.put("avatar", avatar);
		jsonobj.put("direction",dir);
		jsonobj.put("health", health);
		jsonobj.put("items", items);
		jsonobj.put("locX",locX);
		jsonobj.put("locY",locY);
		jsonobj.put("room",roomName);
		
		FileWriter file = null;
		try {
			file = new FileWriter(new File("./playerSave.json"), false);
			file.write(jsonobj.toJSONString());
			file.flush();
			file.close();
			System.out.println("File saved");
		} 
		catch (IOException e){
			e.printStackTrace();
			System.out.println("File not saved");
		} 
	}
	
}
