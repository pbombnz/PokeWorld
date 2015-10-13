package storage;

import java.awt.FileDialog;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JOptionPane;

import ui.ServerFrame;

import com.esotericsoftware.jsonbeans.Json;
import com.esotericsoftware.jsonbeans.OutputType;

import game.Game;

/**
 * A Class that saves the Game's current state into a JSON formatted file
 * 
 * @author Prashant Bhikhu
 * @author Priyanka Bhula
 */
public class GameToJson {

	/**
	 * Saves the Game world
	 * 
	 * @author Prashant Bhikhu
	 * @param parentFrame The current frame the save method is used in so any dialog boxes that appear are centered
	 * @param game The Game world object you would like to save
	 */
	public static void saveGame(ServerFrame parentFrame, Game game) {
		// Creates the saving File Dialog and sets the appropriate 
	    FileDialog fDialog = new FileDialog(parentFrame, "Save Server Game as file..", FileDialog.SAVE);
        fDialog.setDirectory(".");
        fDialog.setFile("game.json");
        fDialog.setVisible(true);
        
        // Once the fDialog is closed, we check  if the user actually picked a file or canceled the save operation
        if(fDialog.getFile() == null) {
        	return; // User canceled save operation, so we no longer need to proceed.
        }
        
        // Get the path of the selected save file as a string
        String path = fDialog.getDirectory() + fDialog.getFile();
        
        //Attempt the create the file at that specified path
        FileWriter fileWriter;
        try {
        	fileWriter = new FileWriter(path, false);
		} catch (IOException e) {
			// Failure to create file
			 parentFrame.writeToConsole("[Server][Save] Saving game Failed.");
			JOptionPane.showMessageDialog(parentFrame, "Cannot Save at this location. Make sure you have correct write permissions.", "ERROR", JOptionPane.ERROR_MESSAGE);
			return;
		}
        
        // Write Game (as JSON) to the file
		BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
			
		// Set JSON write configurations	
	    Json json = new Json();
	    json.setOutputType(OutputType.json);
	    try {
	    	// Finally write the JSON to the file
			bufferedWriter.write(json.toJson(game));
			bufferedWriter.close();
		} catch (IOException e) {
			// Failure to write file
			parentFrame.writeToConsole("[Server][Save] Saving game Failed.");
			JOptionPane.showMessageDialog(parentFrame, "Cannot write at this location. Make sure you have the correct write permissions.", "ERROR", JOptionPane.ERROR_MESSAGE);
			return;
		}
	    parentFrame.writeToConsole("[Server][Save] Saved game Sucessfully. ("+path+")");
	}

	/* PRIYANKA's REDUNDANT CODE
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
