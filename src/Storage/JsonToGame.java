package Storage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import game.Location;
import game.Player;
import game.Player.Direction;
import game.avatar.Avatar;
import game.objects.Item;


public class JsonToGame {
	@SuppressWarnings("unchecked")
	public static Player loadPlayer(){
		JSONParser jp = new JSONParser();
		try{
			Object o = jp.parse(new FileReader(new File("./playerSave.json")));//TODO
			JSONObject jo = (JSONObject) o;
			
			String name = (String) jo.get("name");
			int attack = (int) jo.get("attack");
			Avatar avatar = (Avatar) jo.get("avatar");
			Direction dir = (Direction) jo.get("direction");
			int health = (int) jo.get("health");
			List<Item> items = (List<Item>) jo.get("items");
			Location loc = (Location) jo.get("location");
			
			Player p = new Player(name);
			p.setAttack(attack);
			p.setAvatar(avatar);
			p.setDirection(dir);
			p.setHealth(health);
			for(Item item: items) {
				p.addToInventory(item);
			}
			p.setLocation(loc);
			return p;
		}
		catch (FileNotFoundException e){
			e.printStackTrace();
		}
		catch(IOException e){
			e.printStackTrace();
		}
		catch (ParseException e){
			e.printStackTrace();
		}
		return null;
	}
	
}
	
	
