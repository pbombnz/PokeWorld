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
import game.avatar.Avatar;
import game.objects.interactiveObjects.*;


public class JsonToGame {
	@SuppressWarnings("unchecked")
	public static Player loadPlayer(){
		Player p = new Player();
		JSONParser jp = new JSONParser();
		try{
			Object o = jp.parse(new FileReader("./playerSave.json"));
			JSONObject jo = (JSONObject) o;
			
			String name = (String) jo.get("name");
			System.out.println(name);
		}
		catch (FileNotFoundException e){
			e.printStackTrace();
		} catch (IOException e){
			e.printStackTrace();
		} catch (ParseException e){
			e.printStackTrace();
		}
		return p;
	}
	
}
	
	
