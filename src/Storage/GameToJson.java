package Storage;

import java.util.List;
import java.awt.FileDialog;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;

import javax.swing.JFrame;

import org.json.simple.*;

import com.esotericsoftware.jsonbeans.Json;
import com.esotericsoftware.jsonbeans.OutputType;

import game.Game;
import game.Location;
import game.Player;
import game.Direction;
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

	//PRASHANT SAVING 
	public static void saveGame(JFrame parentFrame, Game game) {
	    Json json = new Json();
	    json.setOutputType(OutputType.json);
	    //String text = json.prettyPrint(game);// json.toJson(game);
	    //System.out.println(text);
	    
	    FileDialog fDialog = new FileDialog(parentFrame, "Save Game as file..", FileDialog.SAVE);
        fDialog.setDirectory(".");
        fDialog.setFile("game.json");
        fDialog.setVisible(true);
        if(fDialog.getFile() == null) {
        	return;
        }
        String path = fDialog.getDirectory() + fDialog.getFile();
        System.out.println(path);
        File f = new File(path);
        try {
			f.createNewFile();
		} catch (IOException e) {
		}
        
        try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(f));
			bw.write(json.toJson(game));
			bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		saveGame(null, Game.createTestMap());
	}
	
	/*
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
		jsonobj.put("newDirection",dir);
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
	}*/
	
}
