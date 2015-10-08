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

import rooms.Room;
import rooms.Room1;
import rooms.Room2;
import rooms.Room3;
import game.Player;
import game.avatar.Avatar;
import ui.GamePlayFrame;

/**
 * The Class represents the model of the state of the game at any time, this represents the
 * rooms on which the game is played depending on the player's location. The Game contains
 * all the locations and items (via nested objects) that make up the world.
 *
 * @author Prashant Bhikhu
 *
 */
public class Game {

	//public static final List<Avatar> allAvatars = Avatar.getAllAvatars();

	private ArrayList<Player> players;
	private ArrayList<Room> rooms;

	/**
	 * no-args constructer (Needed for Kyro Serialisation)
	 * 
	 * Default Constructor. Creates the Game with no players or rooms.
	 * Only used when creating test maps.
	 * 
	 */
	public Game() {
		this.players = new ArrayList<Player>();
		this.rooms = new ArrayList<Room>();
	}

	/**
	 * Creates the Game using preloaded maps and players. Useful for
	 * networking when reading from a Game object byte array.
	 * 
	 * @param players The players that are connected to the game
	 * @param rooms The rooms that are connected to the game
	 */
	public Game(ArrayList<Player> players, ArrayList<Room> rooms) {
		this.players = players;
		this.rooms = rooms;
	}

	/**
	 * Converts this Game to a byte array to send over network
	 *
	 * @return a byteArray
	 */
	public byte[] toByteArray() {
		byte[] bytes = null;
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try {
			ObjectOutputStream oos = new ObjectOutputStream(bos);
			oos.writeObject(this.players);
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
	 * Creates a new Game object (typically an updates version from server) from a byte array.
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
			newGame = new Game(
					(ArrayList<Player>) ois.readObject(),
					(ArrayList<Room>) ois.readObject());
		} catch (StreamCorruptedException e) {
			return null;
		} catch (IOException e) {
			System.err.println("Error recieving the Game from bytes!");
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			System.err.println("Class not found in from bytes. " + e);
			e.printStackTrace();
		}
		return newGame;
	}

	public static Game createTestMap() {
		Game game = new Game();
		//add rooms here
		game.rooms.add(new Room1());
		game.rooms.add(new Room2());
		game.rooms.add(new Room3());
		return game;

	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((players == null) ? 0 : players.hashCode());
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
		if (players == null) {
			if (other.players != null)
				return false;
		} else if (!players.equals(other.players))
			return false;
		if (rooms == null) {
			if (other.rooms != null)
				return false;
		} else if (!rooms.equals(other.rooms))
			return false;
		return true;
	}

	/**
	 * Attempt to match any connected player's id with the given id
	 * 
	 * @param id The id number of the player you want to get
	 * @return if the ID is valid, return the player with the given id, otherwise return null 
	 */
	public Player getPlayerByID(int id) {
		// Only check a player's id if the given id is valid
		if(id < 1) {
			return null;
		}
		
		// Attempt to match any player's id with the given id
		for(Player player: getPlayers()) {
			if(player.getId() == id) {
				// return the player with the given id when found
				return player; 
			}
		}
		// return null if the id wasn't found
		return null;
	}

	/**
	 * @return The players connected to the Game
	 */
	public ArrayList<Player> getPlayers() {
		return players;
	}
	
	/**
	 * @return The rooms connected to the Game
	 */
	public List<Room> getRooms() {
		return rooms;
	}

}
