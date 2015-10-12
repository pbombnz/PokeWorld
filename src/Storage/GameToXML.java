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
	
	public void saveGame(Game g){
		enc.writeObject(g);
	}
	
	public void save(Object o){
		enc.writeObject(o);
	}
	
	public void close(){
		enc.close();
	}
	
}