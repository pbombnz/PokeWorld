package Storage;

import java.util.List;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.json.simple.*;

import game.Board;
import game.Location;
import jdk.nashorn.internal.parser.JSONParser;
import game.Player;
import game.Player.Direction;
import game.avatar.Avatar;
import game.objects.Item;

/*
 * a class that saves the player's current state to a json file
 * 
 * stores all the major variables in the player class
 * 
 * @author Priyanka Bhula
 */
public class GameToJson {
	private JSONObject jsonobj;
	
	@SuppressWarnings("unchecked")
	public void savePlayer(String savePath, Player p) throws IOException, InvalidSaveException{
		String name = p.getName();
		int attack = p.getAttack();
		Avatar avatar = p.getAvatar();
		Direction dir = p.getDirection();
		int health = p.getHealth();
		List<Item> items = p.getInventory();
		Location loc = p.getLocation();	
			
		jsonobj.put("name", name);
		jsonobj.put("attack",attack);
		jsonobj.put("avatar", avatar);
		jsonobj.put("direction",dir);
		jsonobj.put("health", health);
		jsonobj.put("items", items);
		jsonobj.put("location",loc);
		
		FileWriter file = null;
		try {
			file = new FileWriter(savePath+".json");
		} 
		catch (IOException e){
			e.printStackTrace();
		}
	}
	
}
