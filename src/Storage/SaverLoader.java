package Storage;

import java.io.FileNotFoundException;

import javax.swing.JFileChooser;

import game.Game;

/*
 * 
 * @author Pri Bhula
 */
public class SaverLoader{
	private String xmlFile;
	private Game game;
	
	public SaverLoader(){
		XMLToGame loader;
		try{
			loader = new XMLToGame(xmlFile);
			game = loader.loadGame();
		} catch (FileNotFoundException e){
			e.printStackTrace();
		}
	}
	
	public SaverLoader(String path){
		XMLToGame loader;
		try{
			loader = new XMLToGame(path);
			game = loader.loadGame();
		} catch (FileNotFoundException e){
			e.printStackTrace();
		}
	}
	
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
