package Storage;
import java.beans.XMLEncoder;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import game.Game;
import game.Player;

public class GameToXML {
	private BufferedOutputStream buffout;
	private FileOutputStream fileout;
	private XMLEncoder enc;
	
	public GameToXML (String fname) throws FileNotFoundException{
		fileout = new FileOutputStream(fname+".xml");
		buffout = new BufferedOutputStream(fileout);
		enc = new XMLEncoder(buffout);
	}
	
	public GameToXML(File file)throws FileNotFoundException{
		fileout = new FileOutputStream(file);
		buffout = new BufferedOutputStream(fileout);
		enc = new XMLEncoder(buffout);
	}
	
	public void saveGame(Player p, Game g){
		enc.writeObject(p);
		enc.writeObject(g);
		enc.close();
	}
	
}
