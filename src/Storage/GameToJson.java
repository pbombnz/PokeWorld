package Storage;

import java.util.List;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.json.simple.*;

import com.google.*;
import com.google.gson.Gson;

import game.Board;
import game.Location;
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

	
	@SuppressWarnings("unchecked")
	public static void savePlayer(Player p) throws IOException, InvalidSaveException{
		JSONObject jsonobj = new JSONObject();
		
		String name = p.getName();
		Integer attack = p.getAttack();
		Gson gson = new Gson();
		String avatar = gson.toJson( p.getAvatar());
		Direction dir = p.getDirection();
		int health = p.getHealth();
		List<Item> items = p.getInventory();
		String loc = gson.toJson(p.getLocation());	
			
		jsonobj.put("name", name);
		jsonobj.put("attack",attack);
		jsonobj.put("avatar", avatar);
		jsonobj.put("direction",dir.ordinal());
		jsonobj.put("health", health);

		jsonobj.put("items", gson);
		
		jsonobj.put("location",loc);
		
		FileWriter file = null;
		try {
			file = new FileWriter(new File("./playerSave.json"), false);
			file.write(jsonobj.toJSONString());
			file.close();
		} 
		catch (IOException e){
			e.printStackTrace();
		} 
	}
	
}
