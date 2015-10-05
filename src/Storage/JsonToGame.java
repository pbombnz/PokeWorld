package Storage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Map;


import org.json.simple.JSONObject;
import org.json.simple.parser.ContainerFactory;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import game.Location;
import game.Player;
import game.Player.Direction;
import game.avatar.Avatar;
import game.objects.Item;
import game.objects.Key;


public class JsonToGame {
	@SuppressWarnings("unchecked")
	public static Player loadPlayer(){
		return null;
		///JSONParser jp = new JSONParser();
		/*try{
			Object o = new JSONParser().parse(new FileReader(new File("./playerSave.json")));
			JSONObject jo = (JSONObject) o;
			
			
			String name = (String) jo.get("name");
			Integer attack = ((Long) jo.get("attack")).intValue();
			Avatar avatar = (Avatar) gson.fromJson((String) jo.get("avatar"), Avatar.class);
			Direction dir = (Direction) jo.get("direction");
			int health = (int) jo.get("health");
			List<String> items = (List<String>) jo.get("items");
			Location loc = (Location) gson.fromJson((String) jo.get("location"), Location.class);
		
			Player p = new Player(name);
			p.setAttack(attack);
			p.setAvatar(avatar);
			p.setDirection(dir);
			p.setHealth(health);
			for(String item: items) {
				if(item.equals("Key")) {
					p.addToInventory(new Key());
				}
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
	*/
	}
	
}
	
	
