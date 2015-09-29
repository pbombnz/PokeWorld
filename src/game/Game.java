package game;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.StreamCorruptedException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import game.Player;
import game.avatar.Avatar;
import ui.GameFrame;


/**
 *@author Prashant Bhikhu
 */
public class Game {
	
	public static final List<Avatar> allAvatars = Avatar.getAllAvatars();

	public Map<Player, Location> players;
	public List<Room> rooms;

	public Game() {
		players = new HashMap<Player, Location>();
		rooms = new ArrayList<Room>();
		
	}
	
	/**
	 * Creates a new Game (typically an updates version from server) from a
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
	
	private static Game createTestMap() {
		Game game = new Game();
		game.rooms.add(new Room());
		return game;
		
	}
}
