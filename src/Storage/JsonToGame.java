package Storage;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import game.Location;
import game.Player.Direction;
import game.avatar.Avatar;
import game.objects.Item;

public class JsonToGame {
	
	public void loadPlayer(){
		JSONParser jp = new JSONParser();
		try{
			Object o = jp.parse(new FileReader("ADD FILE PATH"));//TODO
			JSONObject jo = (JSONObject) o;
			
			String name = (String) jo.get("name");
			int attack = (int) jo.get("attack");
			Avatar avatar = (Avatar) jo.get("avatar");
			Direction dir = (Direction) jo.get("direction");
			int health = (int) jo.get("health");
			int id = (int) jo.get("id");
			@SuppressWarnings("unchecked")
			List<Item> items = (List<Item>) jo.get("items");
			Location loc = (Location) jo.get("location");
			
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
	}
	
}
	
	
