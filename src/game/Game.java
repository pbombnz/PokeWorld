package game;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.util.ArrayList;
import java.util.Collections;
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
	
	//public static final List<Avatar> allAvatars = Avatar.getAllAvatars();

	public ArrayList<Player> players2;
	public ArrayList<Room> rooms;

	public Game() {
		players2 = new ArrayList<Player>();
		rooms = new ArrayList<Room>();	
	}
	
	public byte[] toByteArray() {
		byte[] bytes = null;
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try {
			ObjectOutputStream oos = new ObjectOutputStream(bos);
			oos.writeObject(this.players2);
			oos.writeObject(this.rooms);
			oos.flush();
			oos.close();
			bos.close();
			bytes = bos.toByteArray();
		} catch (IOException e) {
			System.err.println("Error sending the GameWorld to bytes!");
			e.printStackTrace();
		}
		return bytes;		
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
			newGame.players2 = (ArrayList<Player>) ois.readObject();
			newGame.rooms = (ArrayList<Room>) ois.readObject();
		} catch (StreamCorruptedException e) {
			return null;
		} catch (IOException e) {
			System.err.println("Error recieving the Game from bytes!");
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			System.err.println("Class not found in from bytes. "+e);
			e.printStackTrace();
		}
		return newGame;
	}
	
	public static Game createTestMap() {
		Game game = new Game();
		game.rooms.add(new Room());
		return game;
		
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((players2 == null) ? 0 : players2.hashCode());
		result = prime * result + ((rooms == null) ? 0 : rooms.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Game other = (Game) obj;
		if (players2 == null) {
			if (other.players2 != null)
				return false;
		} else if (!players2.equals(other.players2))
			return false;
		if (rooms == null) {
			if (other.rooms != null)
				return false;
		} else if (!rooms.equals(other.rooms))
			return false;
		return true;
	}
	
	public ArrayList<Player> getPlayers() {
		return players2;
	}

	public List<Room> getRooms() {
		return rooms;
	}
	
}
