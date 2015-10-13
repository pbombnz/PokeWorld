package storage;

import java.awt.FileDialog;
import java.io.File;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.json.simple.JSONObject;
import org.json.simple.parser.ContainerFactory;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import ui.ServerFrame;

import com.esotericsoftware.jsonbeans.Json;
import com.esotericsoftware.jsonbeans.JsonException;
import com.esotericsoftware.jsonbeans.OutputType;

import game.Game;

/**
 * This class loads a Game from a JSON formatted file
 * 
 * @author Prashant Bhikhu
 * @author Priyanka Bhula
 */
public class JsonToGame {
	
	/**
	 * Loads Game from JSON file
	 * 
	 * @param parentFrame
	 * @return The Game Object from the JSON if successful, otherwise return null
	 * @author Prashant Bhikhu
	 */
	public static Game loadGame(ServerFrame parentFrame) {
		// Creates the saving File Dialog and sets the appropriate 
	    FileDialog fDialog = new FileDialog(parentFrame, "Load Saved Game as file..", FileDialog.LOAD);
        fDialog.setDirectory(".");
        fDialog.setFile("game.json");
        fDialog.setVisible(true);
        
        // Once the fDialog is closed, we check  if the user actually picked a file or canceled the load operation
        if(fDialog.getFile() == null) {
        	return null; // User canceled load operation, so we no longer need to proceed.
        }
        
        // Get the path of the selected save file as a string
        String path = fDialog.getDirectory() + fDialog.getFile();

		// Set JSON Read configurations	
	    Json json = new Json();
	    json.setOutputType(OutputType.json);
	    // Read the Game from JSON file
	    Game loadedGame = null;
	    try {
	    	loadedGame = json.fromJson(Game.class, new File(path));
	    } catch(JsonException e) {
	    	// Show error to user if one appears (Usually occurs when user tries to make the server load a non-compatible file)
	    	parentFrame.writeToConsole("[Server][Load] Loading game failed. ("+path+")");
	    	JOptionPane.showMessageDialog(parentFrame, "You can only load Game JSON files only.", "ERROR", JOptionPane.ERROR_MESSAGE);
			return null;
	    }
	    parentFrame.writeToConsole("[Server][Load] Saved game Sucessfully. ("+path+")");
		System.out.println(loadedGame.toString());
		return loadedGame;
	}	
	
	/* PRIYANKA's REDUNDANT CODE
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
	*/
}
	
	