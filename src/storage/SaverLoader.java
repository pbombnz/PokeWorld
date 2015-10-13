package storage;

import java.io.FileNotFoundException;

import javax.swing.JFileChooser;

import game.Game;

/*
 * Helper method to aid in the saving and loading
 * 
 * @author Pri Bhula
 */
public class SaverLoader{
	private String xmlFile;
	private Game game;
	
	/*
	 * loads game from file
	 */
	public SaverLoader(){
		XMLToGame loader;
		try{
			loader = new XMLToGame(xmlFile);
			game = loader.loadGame();
		} catch (FileNotFoundException e){
			e.printStackTrace();
		}
	}
	
	/*
	 * load game, given the file path and name
	 */
	public SaverLoader(String path){
		XMLToGame loader;
		try{
			loader = new XMLToGame(path);
			game = loader.loadGame();
		} catch (FileNotFoundException e){
			e.printStackTrace();
		}
	}
	/*
	 * if the filepath is null, save igame as the default name in the default location, otherwise save as given
	 */
	public void saveGame(String path) throws FileNotFoundException{
		if(path==null){
			GameToXML saver = new GameToXML("saveGame.xml");
			saver.save(game);
			saver.close();
		}
		else{
			GameToXML saver = new GameToXML(path);
			saver.save(game);
			saver.close();
		}
	}
	
	/*
	 * return the savepath given the user's choice
	 */
	public String savePath(){
		JFileChooser fc = new JFileChooser();
		int option = fc.showSaveDialog(null);
		if(option==JFileChooser.APPROVE_OPTION){
			String path = fc.getSelectedFile().getAbsolutePath();
			return path;
		}
		else{
			return null;
		}
	}
	
	/*
	 * return the loadpath given the user's choice
	 */
	public String loadPath(){
		JFileChooser fc = new JFileChooser();
		fc.setDialogTitle("Select Saved XML File");
		int returnVal = fc.showOpenDialog(null);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			String path = fc.getSelectedFile().getAbsolutePath();
			return path;
		} else {
			return null;
		}

	}
	
	

}
