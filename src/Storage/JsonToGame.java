package Storage;

import java.awt.FileDialog;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;




import java.util.Scanner;

import javax.swing.JFrame;

import org.json.simple.JSONObject;
import org.json.simple.parser.ContainerFactory;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.esotericsoftware.jsonbeans.Json;
import com.esotericsoftware.jsonbeans.OutputType;

import game.Game;
import game.Location;
import game.Player;
import game.avatar.Avatar;
import game.objects.interactiveObjects.*;


public class JsonToGame {
	
	//PRASHANT LOADING 
	public static Game loadGame(JFrame parentFrame) {
	    Json json = new Json();
	    json.setOutputType(OutputType.json);
	    
	    FileDialog fDialog = new FileDialog(parentFrame, "Load Game as file..", FileDialog.LOAD);
        fDialog.setDirectory(".");
        fDialog.setFile("game.json");
        fDialog.setVisible(true);
        if(fDialog.getFile() == null) {
        	return null;
        }
        String path = fDialog.getDirectory() + fDialog.getFile();

		Game g = json.fromJson(Game.class, new File(path));
		System.out.println(g.toString());
		return g;
	}	
	
	public static void main(String[] args) {
		loadGame(null);
	}
	/*@SuppressWarnings("unchecked")
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
	*/
}
	
	
