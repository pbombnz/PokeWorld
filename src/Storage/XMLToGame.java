package Storage;

import java.beans.XMLDecoder;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import game.Game;

public class XMLToGame {
	private BufferedInputStream buffins;
	private FileInputStream fileins;
	private XMLDecoder dec;
	
	public XMLToGame(String fname) throws FileNotFoundException{
		fileins = new FileInputStream(fname);
		buffins = new BufferedInputStream(fileins);
		dec = new XMLDecoder(buffins);		
	}
	
	public XMLToGame (File file) throws FileNotFoundException{
		if(file.getName().endsWith(".xml")){
			fileins = new FileInputStream(file);
			buffins = new BufferedInputStream(fileins);
			dec = new XMLDecoder(buffins);
		}
		else{
			throw new IllegalArgumentException("wrong format");
		}
	}
	
	public Game loadGame(){
		return (Game) dec.readObject();
	}

	public Object readObject(){
		return dec.readObject();
	}

	public void close(){
		dec.close();
	}

}