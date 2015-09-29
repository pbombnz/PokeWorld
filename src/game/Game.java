package game;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.StreamCorruptedException;
import java.util.List;

import player.Player;
import ui.GameFrame;


/**
 *@author Wang Zhen
 * game
 */
public class Game {
	
	public List<Player> players;
	public static List<Room> rooms;

	public Game() {
	
	}
	
	/**
	 * Creates a new GameWorld (typically an updates version from server) from a
	 * byte array
	 *
	 * @param bytes
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Game fromByteArray(byte[] bytes) {
		Game newGame = null;
		try {
			ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
			ObjectInputStream ois = new ObjectInputStream(bis);
			newGame = new Game();
					//(HashMap<String, Location>) ois.readObject(),
					//(HashMap<Avatar, Location>) ois.readObject(),
					//(ArrayList<Avatar>) ois.readObject());
		} catch (StreamCorruptedException e) {
			return null;
		} catch (IOException e) {
			System.err.println("Error recieving the Game from bytes!");
			e.printStackTrace();
		} /*catch (ClassNotFoundException e) {
			System.err.println("Class not found in from bytes");
			e.printStackTrace();
		}*/
		return newGame;
	}	
}
